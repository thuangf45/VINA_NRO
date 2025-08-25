<?php
require_once 'set.php';
include 'connect.php';
?>
<head>
	<meta charset="utf-8">
	<meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no">
	<title>Ngọc Rồng Green</title>
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
	<meta name="twitter:image" content="image/logo.png">
	<meta name="twitter:image:width" content="200">
	<meta name="twitter:image:height" content="200">
	<link href="assets/bootstrap/css/bootstrap.min.css" rel="stylesheet">
	<link href="assets/fontawesome-free/css/all.min.css" rel="stylesheet" type="text/css">
	<script src="assets/jquery/jquery.min.js"></script>
	<script src="assets/notify/notify.js"></script>
	<link rel="icon" href="image/icon.png?v=99">
	<link href="assets/main.css" rel="stylesheet">

</head>

<body>
	<div class="container" style="border-radius: 15px; background: #ffaf4c; padding: 0px">
        <div class="container" style="background-color: #e67e22; border-radius: 15px 15px 0px 0px">
            <div class="row bg pb-3 pt-2">
                <div class="col">
                    <div class="text-center mb-2">
                        <a href="../index"><img class="rounded" src="../image/logo.png" id="logo"></a>
                    </div>
                    <div class="text-center pt-2">
                        <div style="display: inline-block;">
                            <a href="../download/nro green.apk"> <img class="icon-download" src="../image/android.png">
                            </a><br>
                            <small class="text-dark">
                                <?php echo $_android; ?>
                            </small>
                        </div>
                        <div style="display: inline-block;">
                            <a href="../download/nro green.rar"><img class="icon-download" src="../image/pc.png">
                            </a><br>
                            <small class="text-dark">
                                <?php echo $_windows; ?>
                            </small>
                        </div>
                        <div style="display: inline-block;">
                        <a href="../download/nro green.ipa"><img class="icon-download" src="../image/ip.png">
						</a><br>
                            <small class="text-dark">
                                <?php echo $_iphone; ?>
                            </small>
                        </div>
                        <div>
                            <img height="12" src="image/12.png" style="vertical-align: middle;">
                            <small style="font-size: 10px" id="hour3">Dành cho người chơi trên 12 tuổi. Chơi quá 180
                                phút mỗi ngày sẽ hại sức khỏe.</small>
                        </div>
                    </div>
                </div>
            </div>
        </div>
		<div class="container color-main2 pb-2">
			<div class="text-center">
				<div class="row">
					 <div class="col pr-0"> <a href="index" class="btn p-1 btn-header">Trang chủ</a> </div>
					<div class="col"> <a href="dien-dan" class="btn p-1 btn-header-active">Diễn đàn</a> </div>
					<div class="col"> <a href="https://ducvps.shop/" class="btn p-1 btn-header-active">Fanpage</a> </div>
					<div class="col"> <a href="https://ducvps.shop/" class="btn p-1 btn-header-active">ducvps.shop</a> </div>
				</div>
			</div>
		</div>
		<style>
			.img-thumbnail {
				padding: 0.25rem;
				background-color: #fff;
				border: 1px solid #dee2e6;
				border-radius: 0.25rem;
				max-width: 100%;
				height: auto;
			}
			#backTop {
			position: fixed;
			bottom: 60px;
			right: 2px;
			z-index: 9999;
			text-align: center;
			text-transform: uppercase;
			cursor: pointer;
			text-decoration: none;
			transition: opacity 0.2s ease-out;
			opacity: 0;
			border: 1px solid #797979;
			border: none;
		}

		#backTop span {
			color: #453c3c;
			display: block;
			font-weight: 900;
		}

		#backTopimg:hover {
			opacity: 0.8 !important;
		}

		#backTop.show {
			opacity: 1;
		}

		@media (max-width: 500px) {
			#backTop.show {
				display: block;
			}
		}
		.snowEffect {
    position: fixed;
    width: 100%;
    height: 100%;
    left: 0;
    top: 0;
    z-index: 10;
    overflow: hidden;
    pointer-events:none; /*Không có dòng này là ko click đc các phần tử khác*/
}
#snowcanvas{
    position: fixed;
    z-index: 0;
}
		</style>
		<div class="container">
			<div 
			<div style="position: relative;" class="body_body">
<a href="#" id="backTop"><img id="backTopimg" src="image/favicon-32x32.png" alt="top" /> </a>
		</style>

		<div class="container">
		<img src="https://ngocrongonline.com/images/baner_TrungThu.png" width="100%">
		<h5 class="text-danger pt-2">Giới Thiệu</h5>
				<div> Ngọc Rồng Online là Trò Chơi Trực Tuyến với cốt truyện xoay quanh bộ truyện tranh 7 viên Ngọc Rồng. Người chơi sẽ hóa thân thành một trong những anh hùng của 3 hành tinh: Trái Đất, Xayda, Namếc. Cùng luyện tập, tăng cường sức mạnh và kỹ năng. Đoàn kết cùng chiến đấu chống lại các thế lực hung ác. Cùng nhau tranh tài.
Đặc điểm nổi bật:
- Thể loại hành động, nhập vai. Trực tiếp điều khiển nhân vật hành động. Dễ chơi, dễ điều khiển nhân vật. Đồ họa sắc nét. Có phiên bản đồ họa cao cho điện thoại mạnh và phiên bản pixel cho máy cấu hình thấp.
- Cốt truyện bám sát nguyên tác. Người chơi sẽ gặp tất cả nhân vật từ Bunma, Quy lão kame, Jacky-chun, Tàu Pảy Pảy... cho đến Fide, Pic, Poc, Xên, Broly, đội Bojack.
- Đặc điểm nổi bật nhất: Tham gia đánh doanh trại độc nhãn. Tham gia đại hội võ thuật. Tham gia săn lùng ngọc rồng để mang lại điều ước cho bản thân.
- Tương thích tất cả các dòng máy trên thị trường hiện nay: Máy tính PC, Điện thoại di động Nokia Java, Android, iPhone, Windows Phone, và máy tính bảng Android, iPad. </div>

</div>
		<p style="color:red;text-align:center;margin-bottom: -10px;r">Cơ Bản</p>
		<p></p>
		
		<p style="text-align:center"><img alt="" src="https://ngocrongonline.com/gif//gif_maphongba.gif" style="">&nbsp;<img alt="" src="https://ngocrongonline.com/gif//gif_gif_Saiyain.gif" style="">&nbsp;<img alt="" src="https://ngocrongonline.com/gif/gif_supber_kame.gif" style="">&nbsp;</p>

		<p style="color:red;text-align:center;margin-bottom: -10px;r">Vip</p>
		<p></p>
<p style="text-align:center"><img alt="" src="https://ngocrongonline.com/gif//gif_maphongba_VIP.gif" style="">&nbsp;<img alt="" src="https://ngocrongonline.com/gif//gif_gif_Saiyain_VIP.gif" style="">&nbsp;<img alt="" src="https://ngocrongonline.com/gif/gif_supber_kame_VIP.gif" style="">&nbsp;</p>
		<p style="color:red;text-align:center;margin-bottom: -10px;r">Các Chiêu Đấm demon</p>
		<p></p>
<p style="text-align:center"><img alt="" src="https://ngocrongonline.com/gif//Td_danhthuong.gif" style="">&nbsp;<img alt="" src="https://ngocrongonline.com/gif//Namec_Danhthuong.gif" style="">&nbsp;<img alt="" src="https://ngocrongonline.com/gif/XayDa_Danhthuong.gif" style="">&nbsp;</p>
			<div class="mt-3 text-center"> <img src="image/hot.gif"> <a href="dang-ky" class="text-dark">Đăng ký ngay</a> <img src="image/hot.gif"> </div>
			<div>
<p style="color:red;text-align:center;margin-bottom: -10px;r">máy chủ được đầu tư vps bởi anh đức</p>
<p></p>
<p style="color:red;text-align:center;margin-bottom: -10px;r">lên anh em lên web anh ấy ủng hộ nha</p>
<p></p>
<p style="color:red;text-align:center;margin-bottom: -10px;r">ducvps.shop</p>
<p></p>
<div>
</div>
<div class="bg-content">
<div>
<div class="title">
<h4>Hướng Dẫn Tân Thủ</h4>
</div>
<div class="content"><p><strong>1. Đăng k&yacute; t&agrave;i khoản</strong></p>
<p>Ngọc Rồng Online sử dụng T&agrave;i Khoản ri&ecirc;ng, kh&ocirc;ng chung với bất kỳ Tr&ograve; Chơi n&agrave;o kh&aacute;c.<br/>
Bạn c&oacute; thể đăng k&yacute; t&agrave;i khoản miễn ph&iacute; ngay trong game, hoặc tr&ecirc;n trang Diễn Đ&agrave;n.<br/>
Khi đăng k&yacute;, bạn n&ecirc;n sử dụng đ&uacute;ng số điện thoại hoặc email thật của m&igrave;nh. Nếu sử dụng th&ocirc;ng tin sai, người c&oacute; số điện thoại hoặc email thật sẽ c&oacute; thể lấy mật khẩu của bạn.<br/>
Số điện thoại v&agrave; email của bạn sẽ kh&ocirc;ng hiện ra cho người kh&aacute;c thấy. Admin kh&ocirc;ng bao giờ hỏi mật khẩu của bạn.</p>
<p><strong>2. Hướng dẫn điều khiển</strong></p>
<p>Đối với m&aacute;y b&agrave;n ph&iacute;m: D&ugrave;ng ph&iacute;m mũi t&ecirc;n, ph&iacute;m số, để điều khiển nh&acirc;n vật. Ph&iacute;m chọn giữa để tương t&aacute;c.<br/>
Đối với m&aacute;y cảm ứng: D&ugrave;ng tay chạm v&agrave;o m&agrave;n h&igrave;nh cảm ứng để di chuyển. Chạm nhanh 2 lần v&agrave;o 1 đối tượng để tương t&aacute;c.<br/>
Đối với PC: D&ugrave;ng chuột, click chuột phải để di chuyển, click chuột tr&aacute;i để chọn, click đ&ocirc;i v&agrave;o đối tượng để tương t&aacute;c</p>
<p><strong>3. Một số th&ocirc;ng tin căn bản</strong></p>
<p>- Đậu thần d&ugrave;ng để tăng KI v&agrave; HP ngay lập tức.<br/>
- Bạn chỉ mang theo người được 10 hạt đậu. Nếu muốn mang nhiều hơn, h&atilde;y xin từ bạn b&egrave; trong Bang.<br/>
- Tất cả c&aacute;c s&aacute;ch kỹ năng đều c&oacute; thể học miễn ph&iacute; tại Quy L&atilde;o Kame, khi bạn c&oacute; đủ điểm tiềm năng.<br/>
- Bạn kh&ocirc;ng thể bay, d&ugrave;ng kỹ năng, nếu hết KI.<br/>
- Tấn c&ocirc;ng qu&aacute;i vật c&ugrave;ng bạn b&egrave; trong Bang sẽ mang lại nhiều điểm tiềm năng hơn đ&aacute;nh một m&igrave;nh.<br/>
- Tập luyện với bạn b&egrave; tại khu vực th&iacute;ch hợp sẽ mang lại nhiều điểm tiềm năng hơn đ&aacute;nh qu&aacute;i vật.<br/>
- Khi được n&acirc;ng cấp, đậu thần sẽ phục hồi nhiều HP v&agrave; KI hơn.<br/>
- V&agrave;o tr&ograve; chơi đều đặn mỗi ng&agrave;y để nhận được Ngọc miễn ph&iacute;.<br/>
- Đ&ugrave;i g&agrave; sẽ phục hồi 100% HP, KI. C&agrave; chua phục hồi 100% KI. C&agrave; rốt phục hồi 100% HP.<br/>
- C&acirc;y đậu thần kết một hạt sau một thời gian, cho d&ugrave; bạn đang offline.<br/>
- Sau 3 ng&agrave;y kh&ocirc;ng tham gia tr&ograve; chơi, bạn sẽ bị giảm sức mạnh do lười luyện tập.<br/>
- Bạn sẽ giảm thể lực khi đ&aacute;nh qu&aacute;i, nhưng sẽ tăng lại thể lực khi kh&ocirc;ng đ&aacute;nh nữa.</p>

</div>

</div>
</div>
<div class="bg-content">
<div>
<div class="title">
<h4>Bạn nên tải phiên bản nào?</h4>
</div>
<div class="content"><p>Nếu bạn d&ugrave;ng điện thoại Nokia cũ, c&oacute; b&agrave;n ph&iacute;m như Nokia 6300, Nokia E72, Nokia X2, Nokia C2, H&atilde;y tải bản JAVA</p>
<p>Nếu bạn d&ugrave;ng m&aacute;y cảm ứng sử dụng Android như: Samsung Galaxy Y, HTC, LG, Sky, HKPhone. H&atilde;y tải bản Android APK hoặc Android Playstore đều được.</p>
<p>Nếu bạn d&ugrave;ng điện thoại cảm ứng của NOKIA Lumia, hoặc c&aacute;c m&aacute;y HTC chạy Windows Phone, h&atilde;y tải bản cho Windows Phone.</p>
<p>Nếu bạn d&ugrave;ng m&aacute;y vi t&iacute;nh c&aacute; nh&acirc;n, laptop chạy Windows XP - Windows 7, h&atilde;y tải bản PC.</p>
<p>Nếu bạn d&ugrave;ng iPhone, iPod, iPad, h&atilde;y tải bản iPhone Appstore. Nếu bạn biết chắc rằng m&aacute;y m&igrave;nh đ&atilde; jailbreak, c&oacute; c&agrave;i AppSync hoặc AppstoreVN, h&atilde;y c&agrave;i từ bản iPhone jailbreak để tốc độ nhanh hơn.</p>
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
				</div>
				<div class="copyright" style="line-height: 7px">

Bản Quyền thuộc về Ngọc Rồng Green

</div>
			</div>
		</div>
	</div>
	</div>
	<script src="assets/bootstrap/js/bootstrap.bundle.min.js"></script>
	<script src="assetsasset/main.js"></script>
	<script type="7a8e2b01fb8ba961288447c0-text/javascript">
		(function($) {

			"use strict"

			$(function() {
				if ($('#backTop').length) {
					var scrollTrigger = 100, // px
						backToTop = function() {
							var scrollTop = $(window).scrollTop();
							if (scrollTop > scrollTrigger) {
								$('#backTop').addClass('show');
							} else {
								$('#backTop').removeClass('show');
							}
						};
					backToTop();
					$(window).on('scroll', function() {
						backToTop();
					});
					$('#backTop').on('click', function(e) {
						e.preventDefault();
						$('html,body').animate({
							scrollTop: 0
						}, 700);
					});
				}

			});

		})(jQuery);
	</script>
<script src="/cdn-cgi/scripts/7d0fa10a/cloudflare-static/rocket-loader.min.js" data-cf-settings="7a8e2b01fb8ba961288447c0-|49" defer></script></body>

</body><!-- Bootstrap core JavaScript -->

</html>