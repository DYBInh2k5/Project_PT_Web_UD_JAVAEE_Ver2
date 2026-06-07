# Yêu cầu Web bán sách

## Công nghệ sử dụng
- Spring Boot MVC
- ThymeLeaf
- SQL Server

## Yêu cầu về dữ liệu
- Nhập 1 admin và 2 khách hàng.
- Ít nhất 30 sản phẩm, mỗi sản phẩm có 1 hình.

## Yêu cầu chức năng của khách hàng
- Đăng ký, đăng nhập, đăng xuất.
- Xem sản phẩm (thực hiện phân trang hợp lý).
- Tìm kiếm sản phẩm theo tên, theo loại.
- Sắp thứ tự theo giá sản phẩm (tăng dần và giảm dần).
- Quản lý giỏ hàng: thêm món hàng vào giỏ hàng, sửa số lượng, xóa món hàng khỏi giỏ hàng, thanh toán (nếu khách hàng chưa login thì yêu cầu login).

## Yêu cầu chức năng của admin
- Đăng nhập, đăng xuất.
- Quản lý sản phẩm: xem, thêm, sửa, xóa.
- Quản lý đơn hàng: xem, cập nhật trạng thái đơn hàng (mới, đã vận chuyển, đã thanh toán).
- Xem doanh thu: cho chọn 1 ngày cụ thể --> cho xem nội dung sau:
    + Tổng doanh thu của ngày được chọn.
    + Tổng doanh thu của tháng được chọn.
    + Tổng doanh thu của năm được chọn.
    + Vẽ biểu đồ dạng cột biểu diễn doanh thu 7 ngày kể từ ngày được chọn trở về trước.
