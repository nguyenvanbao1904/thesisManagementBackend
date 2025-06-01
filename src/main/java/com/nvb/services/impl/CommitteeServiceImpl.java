/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.nvb.services.impl;

import com.nvb.dto.CommitteeDTO;
import com.nvb.dto.CommitteeListDTO;
import com.nvb.dto.CommitteeMemberDTO;
import com.nvb.dto.ThesesDTO;
import com.nvb.dto.UserDTO;
import com.nvb.pojo.AcademicStaff;
import com.nvb.pojo.Committee;
import com.nvb.pojo.CommitteeMember;
import com.nvb.pojo.CommitteeMemberPK;
import com.nvb.pojo.CommitteeMemberRole;
import com.nvb.pojo.CommitteeStatus;
import com.nvb.pojo.EvaluationFinalScore;
import com.nvb.pojo.EvaluationScore;
import com.nvb.pojo.Lecturer;
import com.nvb.pojo.Thesis;
import com.nvb.pojo.ThesisStatus;
import com.nvb.repositories.AcademicsStaffRepository;
import com.nvb.repositories.CommitteeRepository;
import com.nvb.repositories.EvaluationRepository;
import com.nvb.repositories.LecturerRepository;
import com.nvb.repositories.ThesesRepository;
import com.nvb.services.CommitteeService;
import com.nvb.services.EmailService;
import com.nvb.services.ThesesService;
import com.nvb.services.UserService;
import jakarta.mail.MessagingException;
import jakarta.persistence.EntityNotFoundException;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author nguyenvanbao
 */
@Service
@Transactional
public class CommitteeServiceImpl implements CommitteeService {

    @Autowired
    private CommitteeRepository committeeRepository;

    @Autowired
    private AcademicsStaffRepository academicsStaffRepository;

    @Autowired
    private LecturerRepository lecturerRepository;

    @Autowired
    private ThesesRepository thesesRepository;

    @Autowired
    private EvaluationRepository evaluationRepository;

    @Autowired
    private EmailService emailService;

    @Autowired
    private UserService userDetailsService;

    @Autowired
    private ThesesService thesesService;

    @Autowired
    private ModelMapper modelMapper;

    @Override
    public List<CommitteeDTO> getAll(Map<String, String> params) {
        return getAll(params, false, false);
    }

    @Override
    public List<CommitteeDTO> getAll(Map<String, String> params, boolean pagination) {
        return getAll(params, pagination, false);
    }

    @Override
    public List<CommitteeDTO> getAll(Map<String, String> params, boolean pagination, boolean details) {
        return committeeRepository.getAll(params, pagination, details)
                .stream().map(this::toCommitteeDTO).collect(Collectors.toList());
    }

    @Override
    public List<CommitteeListDTO> getAllForListView(Map<String, String> params, boolean pagination) {
        return committeeRepository.getAll(params, pagination, false)
                .stream().map(this::toCommitteeListDTO).collect(Collectors.toList());
    }

    @Override
    public CommitteeDTO get(Map<String, String> params) {
        Committee committee = committeeRepository.get(params);
        return committee != null ? toCommitteeDTO(committee) : null;
    }

    private CommitteeMemberRole parseRole(String roleStr) {
        if (roleStr == null) {
            throw new IllegalArgumentException("Chuỗi vai trò không được null");
        }
        switch (roleStr.toUpperCase()) {
            case "CHAIRMAN":
                return CommitteeMemberRole.ROLE_CHAIRMAN;
            case "SECRETARY":
                return CommitteeMemberRole.ROLE_SECRETARY;
            case "REVIEWER":
                return CommitteeMemberRole.ROLE_REVIEWER;
            case "MEMBER":
                return CommitteeMemberRole.ROLE_MEMBER;
            default:
                return CommitteeMemberRole.valueOf(roleStr.toUpperCase());
        }
    }

    @Override
    public CommitteeDTO addOrUpdate(CommitteeDTO committeeDTO) {
        Committee committee;
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        AcademicStaff academicStaff = null;

        if (authentication != null && authentication.isAuthenticated()) {
            academicStaff = academicsStaffRepository.get(Map.of("username", authentication.getName()));
        }

        List<Integer> dtoLecturerIds = Optional.ofNullable(committeeDTO.getMemberLecturerId())
                .map(ids -> Arrays.stream(ids).filter(Objects::nonNull).collect(Collectors.toList()))
                .orElse(Collections.emptyList());

        Map<Integer, Lecturer> lecturerMap = new LinkedHashMap<>();
        if (!dtoLecturerIds.isEmpty()) {
            Map<Integer, Lecturer> fetchedLecturers = lecturerRepository.getByIds(dtoLecturerIds).stream()
                    .collect(Collectors.toMap(Lecturer::getId, l -> l));

            for (Integer id : dtoLecturerIds) {
                Lecturer lecturer = fetchedLecturers.get(id);
                if (lecturer != null) {
                    lecturerMap.put(id, lecturer);
                }
            }
        }

        // Chuan bi map Thesis
        Set<Integer> dtoThesesIds = Optional.ofNullable(committeeDTO.getThesesIds())
                .map(ids -> Arrays.stream(ids).filter(Objects::nonNull).collect(Collectors.toSet()))
                .orElse(Collections.emptySet());

        Map<Integer, Thesis> thesesMap = dtoThesesIds.isEmpty() ? new HashMap<>()
                : thesesRepository.getByIds(new ArrayList<>(dtoThesesIds)).stream()
                        .collect(Collectors.toMap(Thesis::getId, t -> t));

        if (committeeDTO.getId() == null) {

            committee = new Committee();
            committee.setStatus(CommitteeStatus.LOCKED.toString());
            committee.setCommitteeMembers(new HashSet<>());
            committee.setTheses(new HashSet<>());
            committee.setIsActive(true);

        } else {
            committee = committeeRepository.get(Map.of("id", String.valueOf(committeeDTO.getId())));
            if (committee == null) {
                throw new EntityNotFoundException("Không tìm thấy hội đồng với ID: " + committeeDTO.getId());
            }

            // Xóa các thành viên cũ khỏi collection
            committee.getCommitteeMembers().clear();

            // Xóa các thesis khỏi collection và cập nhật tham chiếu
            if (committee.getTheses() != null) {
                committee.getTheses().forEach(thesis -> thesis.setCommitteeId(null));
                committee.getTheses().clear();
            }
            if(committeeDTO.getStatus() != null){
                committee.setStatus(committeeDTO.getStatus());
            }else{
                committee.setStatus(CommitteeStatus.LOCKED.toString());
            }
        }

        committee.setLocation(committeeDTO.getLocation());
        committee.setCreatedBy(academicStaff);
        committee.setDefenseDate(committeeDTO.getDefenseDate());

        // Lưu Committee trước để có ID
        committee = committeeRepository.addOrUpdate(committee);

        if (committeeDTO.getMemberLecturerId() != null && committeeDTO.getMemberRole() != null) {
            for (int i = 0; i < committeeDTO.getMemberLecturerId().length; i++) {
                Integer lecturerId = committeeDTO.getMemberLecturerId()[i];
                String roleStr = committeeDTO.getMemberRole()[i];
                if (lecturerId == null || roleStr == null) {
                    continue;
                }

                Lecturer lecturer = lecturerMap.get(lecturerId);
                if (lecturer == null) {
                    throw new EntityNotFoundException("Không tìm thấy giảng viên với ID: " + lecturerId);
                }

                CommitteeMember member = new CommitteeMember();
                // Sử dụng ID của committee đã lưu
                member.setCommitteeMemberPK(new CommitteeMemberPK(committee.getId(), lecturerId));
                member.setRole(parseRole(roleStr).name());
                member.setLecturer(lecturer);
                member.setCommittee(committee);
                committee.getCommitteeMembers().add(member);
            }
        }

        // Thêm luận văn
        if (committeeDTO.getThesesIds() != null) {
            for (Integer thesisId : committeeDTO.getThesesIds()) {
                if (thesisId == null) {
                    continue;
                }
                Thesis thesis = thesesMap.get(thesisId);
                if (thesis != null) {
                    thesis.setCommitteeId(committee);
                    committee.getTheses().add(thesis);
                } else {
                    System.err.println("Cảnh báo: Không tìm thấy luận văn với ID " + thesisId);
                }
            }
        }

        Committee savedCommittee = committeeRepository.addOrUpdate(committee);
        return this.toCommitteeDTO(savedCommittee);
    }

    @Override
    public void delete(int id) {
        committeeRepository.delete(id);
    }

    public CommitteeDTO toCommitteeDTO(Committee committee) {
        CommitteeDTO dto = new CommitteeDTO();

        dto.setId(committee.getId());
        dto.setDefenseDate(committee.getDefenseDate());
        dto.setLocation(committee.getLocation());
        dto.setStatus(committee.getStatus());
        dto.setIsActive(committee.isIsActive());

        // Map createdBy
        if (committee.getCreatedBy() != null) {
            dto.setCreatedByName(committee.getCreatedBy().getFirstName() + " " + committee.getCreatedBy().getLastName());
        }

        // Map committee members
        if (committee.getCommitteeMembers() != null) {
            List<String> memberRoles = new ArrayList<>();
            List<CommitteeMemberDTO> memberDTOs = committee.getCommitteeMembers().stream()
                    .map(member -> {
                        CommitteeMemberDTO memberDTO = new CommitteeMemberDTO();
                        memberDTO.setLecturerId(member.getLecturer().getId());
                        memberDTO.setLecturerName(member.getLecturer().getFirstName() + " " + member.getLecturer().getLastName());
                        memberDTO.setRole(member.getRole());
                        memberRoles.add(member.getRole());
                        return memberDTO;
                    })
                    .collect(Collectors.toList());
            dto.setCommitteeMembers(memberDTOs);
            dto.setMemberRole(memberRoles.toArray(new String[0]));
        }

        // Map thesis
        if (committee.getTheses() != null) {
            List<ThesesDTO> thesesDTOs = committee.getTheses().stream()
                    .map(thesis -> {
                        return thesesService.toDTO(thesis);
                    })
                    .collect(Collectors.toList());
            dto.setTheses(thesesDTOs);
        }

            // Map members IDs
            if (committee.getCommitteeMembers() != null) {
                List<Integer> memberIds = committee.getCommitteeMembers().stream()
                        .map(member -> member.getLecturer().getId())
                        .collect(Collectors.toList());
                dto.setMemberLecturerId(memberIds.toArray(new Integer[0]));
            }

            // Map thesis IDs
            if (committee.getTheses() != null) {
                List<Integer> thesisIds = committee.getTheses().stream()
                        .map(Thesis::getId)
                        .collect(Collectors.toList());
                dto.setThesesIds(thesisIds.toArray(new Integer[0]));
            }

            return dto;
        }

    private CommitteeListDTO toCommitteeListDTO(Committee committee) {
        CommitteeListDTO dto = modelMapper.map(committee, CommitteeListDTO.class);
        if (committee.getCreatedBy() != null && committee.getCreatedBy() != null) {
            dto.setCreatedByName(committee.getCreatedBy().getFirstName() + " " + committee.getCreatedBy().getLastName());
        }
        return dto;
    }

    //  schedule
    @Override
    public int activateCommitteesBeforeDefense(Date now, Date targetTime) {
        // Convert Date to LocalDateTime
        LocalDateTime nowLDT = now.toInstant()
                .atZone(ZoneId.systemDefault()).toLocalDateTime();
        LocalDateTime targetLDT = targetTime.toInstant()
                .atZone(ZoneId.systemDefault()).toLocalDateTime();

        List<Committee> committees = committeeRepository
                .findLockedCommitteesInTimeRange(nowLDT, targetLDT);

        for (Committee committee : committees) {
            committee.setStatus(CommitteeStatus.ACTIVE.toString());
            committee.getTheses().forEach(e -> e.setStatus(ThesisStatus.IN_PROGRESS.toString()));
            committeeRepository.addOrUpdate(committee);
        }

        return committees.size();
    }

    @Override
    public int lockCommitteesAfterDefense(Date lockTime) {
        LocalDateTime lockLDT = lockTime.toInstant()
                .atZone(ZoneId.systemDefault()).toLocalDateTime();

        List<Committee> committees = committeeRepository
                .findActiveCommitteesAfterDefense(lockLDT);

        for (Committee committee : committees) {
            committee.setStatus(CommitteeStatus.LOCKED.toString());

            // Lấy danh sách thesisId để xử lý, không xử lý entity trực tiếp
            Set<Integer> thesisIds = committee.getTheses().stream()
                    .map(Thesis::getId)
                    .collect(Collectors.toSet());

            // Cập nhật trạng thái khóa luận
            committee.getTheses().forEach(e -> e.setStatus(ThesisStatus.COMPLETED.toString()));
            committeeRepository.addOrUpdate(committee);

            // Tính điểm trung bình cho từng khóa luận
            for (Integer thesisId : thesisIds) {
                calculateAndSaveFinalScore(thesisId);
            }
        }

        return committees.size();
    }

    @Override
    public int activateMissedCommittees(Date now, Date futureTime) {
        LocalDateTime nowLDT = now.toInstant()
                .atZone(ZoneId.systemDefault()).toLocalDateTime();

        // Tính thời gian 30 phút trước now
        LocalDateTime pastTime = nowLDT.minusMinutes(30);

        List<Committee> committees = committeeRepository
                .findMissedCommittees(pastTime, nowLDT);

        for (Committee committee : committees) {
            committee.setStatus(CommitteeStatus.ACTIVE.toString());
            committee.getTheses().forEach(e -> e.setStatus(ThesisStatus.IN_PROGRESS.toString()));
            committeeRepository.addOrUpdate(committee);
        }

        return committees.size();
    }

    @Override
    public int lockOverdueCommittees(Date lockTime) {
        LocalDateTime lockLDT = lockTime.toInstant()
                .atZone(ZoneId.systemDefault()).toLocalDateTime();

        List<Committee> committees = committeeRepository
                .findOverdueCommittees(lockLDT);

        for (Committee committee : committees) {
            committee.setStatus(CommitteeStatus.LOCKED.toString());
            // Lấy danh sách thesisId để xử lý, không xử lý entity trực tiếp
            Set<Integer> thesisIds = committee.getTheses().stream()
                    .map(Thesis::getId)
                    .collect(Collectors.toSet());

            // Cập nhật trạng thái khóa luận
            committee.getTheses().forEach(e -> e.setStatus(ThesisStatus.COMPLETED.toString()));
            committeeRepository.addOrUpdate(committee);

            // Tính điểm trung bình cho từng khóa luận
            for (Integer thesisId : thesisIds) {
                calculateAndSaveFinalScore(thesisId);
            }
        }

        return committees.size();
    }

    // Phương thức tính điểm và lưu, nhận thesisId thay vì entity
    private void calculateAndSaveFinalScore(Integer thesisId) {
        // Tính điểm trung bình từ repository
        Float averageScore = evaluationRepository.calculateAverageScore(thesisId);

        if (averageScore != null) {
            Thesis thesis = thesesRepository.get(new HashMap<>(Map.of("id", thesisId.toString())));
            if (thesis == null) {
                return;
            }

            // Lấy comment của chairman
            String chairmanComment = null;
            Committee committee = thesis.getCommitteeId();
            if (committee != null) {
                // Tìm chairman trong hội đồng
                CommitteeMember chairman = committee.getCommitteeMembers().stream()
                        .filter(member -> "ROLE_CHAIRMAN".equals(member.getRole()))
                        .findFirst()
                        .orElse(null);

                if (chairman != null) {
                    Integer chairmanId = chairman.getLecturer().getId();

                    // Lấy tất cả comments của chairman
                    Map<String, String> params = new HashMap<>();
                    params.put("thesisId", thesisId.toString());
                    params.put("lecturerId", chairmanId.toString());

                    List<EvaluationScore> chairmanScores = evaluationRepository.getEvaluation(params);

                    // Ghép tất cả comments lại
                    StringBuilder commentBuilder = new StringBuilder();
                    for (EvaluationScore score : chairmanScores) {
                        if (score.getComment() != null && !score.getComment().isEmpty()) {
                            if (commentBuilder.length() > 0) {
                                commentBuilder.append("\n");
                            }
                            commentBuilder.append("Tiêu chí '")
                                    .append(score.getEvaluationCriteria().getName())
                                    .append("': ")
                                    .append(score.getComment());
                        }
                    }

                    chairmanComment = commentBuilder.length() > 0 ? commentBuilder.toString() : null;
                }
            }

            EvaluationFinalScore finalScore = thesis.getEvaluationFinalScore();
            if (finalScore == null) {
                finalScore = new EvaluationFinalScore();
                finalScore.setThesis(thesis);
                finalScore.setThesisId(thesisId);
            }

            finalScore.setAverageScore(averageScore);
            finalScore.setChairmanComment(chairmanComment);
            String cm = chairmanComment;
            thesis.getStudents().forEach(e -> {
                UserDTO student = userDetailsService.get(Map.of("id", e.getId().toString()));
                if (student != null && student.getEmail() != null) {
                    String subject = "Kết quả khóa luận tốt nghiệp";

                    String body = String.format(
                            "<h3>Thân gửi %s,</h3>"
                            + "<p>Bạn đã hoàn thành khóa luận với đề tài: <strong>%s</strong>.</p>"
                            + "<p><strong>Điểm trung bình:</strong> %.2f</p>"
                            + (cm != null
                                    ? "<p><strong>Nhận xét của Chủ tịch hội đồng:</strong></p><pre>%s</pre>"
                                    : "<p><em>Không có nhận xét từ Chủ tịch hội đồng.</em></p>"),
                            student.getFullName(),
                            thesis.getTitle(),
                            averageScore,
                            cm
                    );

                    try {
                        emailService.sendEmail(student.getEmail(), subject, body);
                    } catch (MessagingException ex) {
                        System.getLogger(CommitteeServiceImpl.class.getName()).log(System.Logger.Level.ERROR, (String) null, ex);
                    }
                }
            });
            thesis.setStatus(ThesisStatus.COMPLETED.toString());
            // Lưu vào database
            evaluationRepository.addOrUpdateFinalScore(finalScore);
        }
    }

    @Override
    public CommitteeDTO updateStatus(CommitteeDTO existing, String status) {
        try {
            CommitteeStatus.valueOf(status);
            existing.setStatus(status);

            if (existing.getStatus().equals(CommitteeStatus.LOCKED.toString())) {
                if (existing.getTheses() != null) {
                    existing.getTheses().forEach(e -> {
                        this.calculateAndSaveFinalScore(e.getId());
                    });

                }
            }
            return this.addOrUpdate(existing);

        } catch (IllegalArgumentException ex) {
            return null;
        }
    }
}
