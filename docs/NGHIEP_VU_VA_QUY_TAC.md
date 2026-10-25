# Nghiệp vụ và quy tắc mặc định

- Một bàn chỉ có tối đa một order đang hoạt động.
- Order chưa gửi bếp có thể hủy toàn bộ.
- Chỉ món ở trạng thái `CHO_LAM` mới được hủy trực tiếp.
- Món hoàn thành sẽ trừ kho đúng một lần theo `CongThucMonAn` và cờ `DaTruKho`.
- Không cho hoàn thành món khi kho không đủ nguyên liệu.
- Chỉ chuyển order sang chờ thanh toán khi mọi món đã hoàn thành hoặc bị hủy.
- Mỗi hóa đơn áp dụng tối đa một chương trình khuyến mãi.
- QR/chuyển khoản/thẻ được thu ngân xác nhận thủ công; không tích hợp ngân hàng thật.
- Hỗ trợ thanh toán kết hợp bằng nhiều dòng trong bảng `ThanhToan`.
- Mỗi 10.000 đồng thanh toán được 1 điểm; 1 điểm đổi 1.000 đồng. Có thể sửa trong `application.properties`.
- Điểm được tính theo số tiền cuối cùng sau giảm giá, VAT và điểm đã đổi.
- Khách vãng lai được thanh toán nhưng không tích/đổi điểm.
