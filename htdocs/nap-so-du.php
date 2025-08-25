<?php
include_once 'set.php';
include_once 'connect.php';


$_alert = null;
include('head.php');
if ($_login === null) {
    // Chưa đăng nhập, chuyển hướng đến trang khác bằng JavaScript
    echo '<script>window.location.href = "../dang-nhap";</script>';
    exit(); // Đảm bảo dừng thực thi code sau khi chuyển hướng
}
?>
<!DOCTYPE html>
<html>

<head>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no">
    <title>Trang Chủ Chính Thức - Ngọc Rồng Green</title>
    <meta name="description" content="">
    <meta name="author" content="">
    <meta name="description"
        content="Website chính thức của Chú Bé Rồng Online – Game Bay Vien Ngoc Rong Mobile nhập vai trực tuyến trên máy tính và điện thoại về Game 7 Viên Ngọc Rồng hấp dẫn nhất hiện nay!">
    <meta name="keywords"
        content="Chú Bé Rồng Online,ngoc rong mobile, game ngoc rong, game 7 vien ngoc rong, game bay vien ngoc rong">
    <meta name="twitter:card" content="summary">
    <meta name="twitter:title"
        content="Website chính thức của Chú Bé Rồng Online – Game Bay Vien Ngoc Rong Mobile nhập vai trực tuyến trên máy tính và điện thoại về Game 7 Viên Ngọc Rồng hấp dẫn nhất hiện nay!">
    <meta name="twitter:description"
        content="Website chính thức của Chú Bé Rồng Online – Game Bay Vien Ngoc Rong Mobile nhập vai trực tuyến trên máy tính và điện thoại về Game 7 Viên Ngọc Rồng hấp dẫn nhất hiện nay!">
    <meta name="twitter:image" content="image/logo.jpg">
    <meta name="twitter:image:width" content="200">
    <meta name="twitter:image:height" content="200">
    <link href="assets/bootstrap/css/bootstrap.min.css" rel="stylesheet">
    <link href="assets/fontawesome-free/css/all.min.css" rel="stylesheet" type="text/css">
    <script src="assets/jquery/jquery.min.js"></script>
    <script src="assets/notify/notify.js"></script>
    <link rel="icon" href="image/icon.png?v=99">
    <link href="assets/main.css" rel="stylesheet">
    <script src="https://unpkg.com/sweetalert/dist/sweetalert.min.js"></script>
    <link rel="stylesheet" href="https://cdn.rawgit.com/daneden/animate.css/v3.1.0/animate.min.css">
    <script src='https://cdn.rawgit.com/matthieua/WOW/1.0.1/dist/wow.min.js'></script>
    <link rel="stylesheet" href="https://cdn.rawgit.com/t4t5/sweetalert/v0.2.0/lib/sweet-alert.css">
    <link rel="stylesheet" href="https://use.fontawesome.com/releases/v5.3.1/css/all.css" crossorigin="anonymous">
    <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.4.1/jquery.min.js"></script>
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/toastr.js/latest/css/toastr.min.css" />
    <script src="https://cdnjs.cloudflare.com/ajax/libs/toastr.js/latest/js/toastr.min.js"></script>
</head>


    <div class="container color-forum pt-1 pb-1">
        <div class="row">
            <div class="col">
                <a href="index" style="color: white">Quay lại diễn đàn</a>
            </div>
        </div>
    </div>
    <div class="container pt-3 pb-5">
        <div class="row">
            <div class="col-lg-6 offset-lg-3">
                <div class="text-center pb-3"> <a href="history" class="text-dark"><i
                            class="fas fa-hand-point-right"></i>
                        Xem tình trạng nạp thẻ <i class="fas fa-hand-point-left"></i></a> </div>
                <h4>NẠP SỐ DƯ</h4>
                <?php if ($_login === null) { ?>
                    <p>Bạn chưa đăng nhập? hãy đăng nhập để sử dụng chức năng này</p>
                <?php } else { ?>
                    <script type="text/javascript">
                        new WOW().init();
                    </script>
                    <form method="POST" action="" id="myform">
                        <tbody>
                            <label>Tài Khoản: </label><br>
                            <input type="text" class="form-control form-control-alternative"
                                style="background-color: #DCDCDC; font-weight: bold; color: #696969" name="username" value="<?php echo $_username; ?>
" readonly required>

                            <label>Loại thẻ:</label>
                            <select class="form-control form-control-alternative" name="card_type" required>
                                <option value="">Chọn loại thẻ</option>
                                <?php
                                $cdurl = curl_init("https://thesieutoc.net/card_info.php");
                                curl_setopt($cdurl, CURLOPT_FAILONERROR, true);
                                curl_setopt($cdurl, CURLOPT_FOLLOWLOCATION, true);
                                curl_setopt($cdurl, CURLOPT_RETURNTRANSFER, true);
                                curl_setopt($cdurl, CURLOPT_CAINFO, __DIR__ . '/api/curl-ca-bundle.crt');
                                curl_setopt($cdurl, CURLOPT_CAPATH, __DIR__ . '/api/curl-ca-bundle.crt');
                                $obj = json_decode(curl_exec($cdurl), true);
                                curl_close($cdurl);
                                $length = count($obj);
                                for ($i = 0; $i < $length; $i++) {
                                    if ($obj[$i]['status'] == 1) {
                                        echo '<option value="' . $obj[$i]['name'] . '">' . $obj[$i]['name'] . ' (' . $obj[$i]['chietkhau'] . '%)</option> ';
                                    }
                                }
                                ?>
                            </select>
                            <label>Mệnh giá:</label>
                            <select class="form-control form-control-alternative" name="card_amount" required>
                                <option value="">Chọn mệnh giá</option>
                                <option value="10000">10.000</option>
                                <option value="20000">20.000</option>
                                <option value="30000">30.000 </option>
                                <option value="50000">50.000</option>
                                <option value="100000">100.000</option>
                                <option value="200000">200.000</option>
                                <option value="300000">300.000</option>
                                <option value="500000">500.000</option>
                                <option value="1000000">1.000.000</option>
                            </select>
                            <label>Số seri:</label>
                            <input type="text" class="form-control form-control-alternative" name="serial" required />
                            <label>Mã thẻ:</label>
                            <input type="text" class="form-control form-control-alternative" name="pin" required /><br>
                            <button type="submit" class="btn btn-main form-control" name="submit">NẠP NGAY</button>

                        </tbody>
                    </form>
                    <script type="text/javascript">
                        $(document).ready(function () {
                            var lastSubmitTime = 0;
                            $("#myform").submit(function (e) {
                                var now = new Date().getTime();
                                if (now - lastSubmitTime < 5000) {
                                    Swal.fire({
                                        title: 'Thông báo',
                                        text: 'Vui lòng đợi ít nhất 5 giây trước khi nạp tiếp',
                                        icon: 'error'
                                    });
                                    return false;
                                }
                                lastSubmitTime = now;

                                $("#status").html("");
                                e.preventDefault();
                                $.ajax({
                                    url: "./ajax/card.php",
                                    type: 'post',
                                    data: $("#myform").serialize(),
                                    success: function (data) {
                                        $("#status").html(data);
                                        document.getElementById("myform").reset();
                                        $("#load_hs").load("./history.php");
                                    }
                                });
                            });
                        });
                    </script>

                <?php } ?>
            </div>
        </div>
    </div>
    <div class="border-secondary border-top"></div>
    <div class="container pt-4 pb-4 text-white">
        <div class="row">
            <div class="col">
                <div class="text-center">
                    <div style="font-size: 13px" class="text-dark">
                        <?php
                        ?>
                    </div>
                </div>
				<div class="copyright" style="line-height: 7px">

Bản Quyền thuộc về Ngọc Rồng Green

</div>
            </div>
        </div>
    </div>
    </div>
    <div id="status"></div>

    <script src="https://cdnjs.cloudflare.com/ajax/libs/jquery/2.2.4/jquery.min.js"></script>
    <script src="https://cdnjs.cloudflare.com/ajax/libs/crypto-js/4.0.0/crypto-js.min.js"></script>
    <!-- Code made in tui 127.0.0.1 -->
    <script src="https://cdnjs.cloudflare.com/ajax/libs/popper.js/1.14.3/umd/popper.min.js"
        integrity="sha384-ZMP7rVo3mIykV+2+9J3UJ46jBk0WLaUAdn689aCwoqbBJiSnjAK/l8WvCWPIPm49" crossorigin="anonymous">
        </script>
    <script src="https://stackpath.bootstrapcdn.com/bootstrap/4.1.1/js/bootstrap.min.js"
        integrity="sha384-smHYKdLADwkXOn1EmN1qk/HfnUcbVRZyYmZ4qpPea6sjB/pTJ0euyQp0Mk8ck+5T" crossorigin="anonymous">
        </script>

    <script src="assets/bootstrap/js/bootstrap.bundle.min.js"></script>
    <script src="assets/main.js"></script>
</body><!-- Bootstrap core JavaScript -->

</html>