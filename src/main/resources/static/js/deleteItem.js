function deleteItem(endpoint, id) {
    if (confirm("Bạn chắc chắn xóa không?") === true) {
        fetch(endpoint + id, {
            method: "delete",
            headers: {
                'Accept': 'application/json',
                'Content-Type': 'application/json'
            }
        })
        .then(res => {
            if (res.status === 204) {
                alert("Xóa thành công!");
                location.reload();
            } else if (res.status === 409) { 
                return res.json().then(data => {
                    const errorMessage = data.error || "Lỗi không xác định từ server.";
                    alert(errorMessage);
                }).catch(jsonError => {
                    console.error("Lỗi khi parse JSON response:", jsonError);
                    alert("Lỗi xử lý phản hồi từ server (không phải JSON hợp lệ).");
                });
            } else {
                res.text().then(textData => {
                    alert("Có lỗi xảy ra! Mã lỗi: " + res.status + ". Chi tiết: " + textData);
                }).catch(textError => {
                    alert("Có lỗi xảy ra! Mã lỗi: " + res.status + ". Không thể đọc chi tiết lỗi.");
                });
            }
        })
        .catch(err => {
            console.error("Lỗi khi gọi API:", err);
            alert("Không thể kết nối đến máy chủ hoặc đã có lỗi xảy ra.");
        });
    }
}