<?php
require_once 'set.php';
include 'connect.php';
?>
<head>
	<meta charset="utf-8">
	<meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no">
	<title>Ngọc Rồng Umi</title>
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
	<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/js/bootstrap.bundle.min.js" integrity="sha384-C6RzsynM9kWDrMNeT87bh95OGNyZPhcTNXj1NW7RuBCsyN/o0jlpcV8Qyq46cDfL" crossorigin="anonymous"></script>
	<script src="https://cdn.jsdelivr.net/npm/@popperjs/core@2.11.8/dist/umd/popper.min.js" integrity="sha384-I7E8VVD/ismYTF4hNIPjVp/Zjvgyol6VFvRkX/vR+Vc4jQkC+hVqc2pM8ODewa9r" crossorigin="anonymous"></script>
<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/js/bootstrap.min.js" integrity="sha384-BBtl+eGJRgqQAUMxJ7pMwbEyER4l1g+O15P+16Ep7Q9Q+zqX6gSbd85u4mG4QzX+" crossorigin="anonymous"></script>
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
                            <a href="../download/Umi.apk"> <img class="icon-download" src="../image/android.png">
                            </a><br>
                            <small class="text-dark">
                                <?php echo $_android; ?>
                            </small>
                        </div>
                        <div style="display: inline-block;">
                            <a href="../download/Umi.rar"><img class="icon-download" src="../image/pc.png">
                            </a><br>
                            <small class="text-dark">
                                <?php echo $_windows; ?>
                            </small>
                        </div>
                        <div style="display: inline-block;">
                        <a href="../download/Umi.ipa"><img class="icon-download" src="../image/ip.png">
						</a><br>
                            <small class="text-dark">
                                <?php echo $_iphone; ?>
                            </small>
                        </div>
                        <div style="display: inline-block;">
                        <a href="https://zalo.me/g/glvrwf465"><img class="icon-download" src="../image/zalo.png">
						</a><br>
                            <small class="text-dark">
                                <?php echo $_Zalo; ?>
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
					<div class="col pr-0"> <a href="gioi-thieu" class="btn p-1 btn-header-active">Giới thiệu</a> </div>
					<div class="col"> <a href="dien-dan" class="btn p-1 btn-header">Diễn đàn</a> </div>
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
<p><span style="font-size:medium"><strong>Sơ lược về Chú Bé Rồng Online</strong></span></p>
<p>Chú Bé Rồng Online – game nhập vai trực tuyến với cốt truyện và nhân vật dựa trên bộ truyện tranh nổi tiếng Nhật Bản Dragon Ball đã từng làm say lòng bao nhiêu thế hệ độc giả Việt Nam. Bạn sẽ chọn theo hành tinh nào, Trái Đất, Na mếc hay Xay da? Cuộc hành trình tìm kiếm ngọc rồng và chống kẻ hung ác sẽ bắt đầu nằm trong tay bạn.</p>
<p>Cùng với sự hướng dẫn của các bậc tiền bối và sự nỗ lực của bản thân, bạn có thể đạt đến sức mạnh kinh hoàng, trở thành những chiến binh siêu hạng. Ngoài ra bạn sẽ không phải chiến đấu đơn độc khi xung quanh bạn là những chiến binh cùng chí hướng, cùng hỗ trợ lẫn nhau đối đầu với các thế lực hắc ám.</p>
<p>Ngọc Rồng là trò chơi trực tuyến đa nền tảng. Bạn có thể chơi được trên máy tính PC Windows, iPhone, Các dòng máy chạy hệ điều hành Android, Windows Phone, và có cả bản Java chạy trên S40, S60 cũ của Nokia. Với chất lượng cao và tốc độ mượt mà trên các loại đường truyền mạng ADSL, 3G, GPRS.</p>
<p>Trò chơi thích hợp với mọi lứa tuổi. Điều khiển trực tiếp nhân vật rất dễ dàng trên màn hình cảm ứng. Khi chơi trên PC bạn chỉ cần dùng chuột, hoặc linh hoạt điều khiển nhân vật với bàn phím cứng điện thoại Nokia S40, S60 cũ.</p>
<p><span style="font-size:medium"><strong>Giới thiệu chung:</strong></span></p>
<p style="color: #012699;"><strong>1. Class nhân vật:</strong></p>
<p>Game được làm dựa trên cốt truyện của bộ truyện tranh nổi tiếng Dragon Ball. Khi tham gia vào thế giới Chú Bé Rồng Online, bạn có thể chọn tham gia vào 1 trong 3 hành tinh: Trái Đất, Namếc, Xayda với hình dạng và những khả năng riêng biệt.</p>
<div id="carouselExampleAutoplaying" class="carousel slide" data-bs-ride="carousel">
  <div class="carousel-inner">
    <div class="carousel-item active">
      <img src="image/1.jpg" class="d-block w-100" alt="...">
    </div>
    <div class="carousel-item">
      <img src="image/2.jpg" class="d-block w-100" alt="...">
    </div>
    <div class="carousel-item">
      <img src="image/3.jpg" class="d-block w-100" alt="...">
    </div>
  </div>
  </button>
</div>
<p style="color: #012699;"><strong>2. Hệ thống nhà:</strong></p>
<p style="text-align:center"><img alt src="../image/10.jpg" style="max-width:100%"></p>
<p>- Nguồn hồi phục KI và HP của các chiến binh chính là bằng đậu thần. Đậu thần có thể nâng cấp được, và mỗi lần nâng cấp sẽ tốn một lượng vàng nhất định. Đậu cấp càng cao thì khả năng hồi phục càng nhiều.</p>
<p>- Rương đồ dùng để chứa tài sản quý giá không tiện mang theo người.</p>
<p>&nbsp;</p>
<p style="color: #012699;"><strong>3. Hệ thống map và NPC đa dạng:</strong></p>
<p style="text-align:left">- Những NPC nổi tiếng và gắn liền với cốt truyện của Dragon Ball. Thông qua các NPC đặc biệt như Thượng Đế, Thần Mèo, Thần Vũ Trụ, bạn có khả năng tăng sức mạnh và tiềm năng của nhân vật.</p>
<div id="carouselExampleIndicators" class="carousel slide">
  <div class="carousel-indicators">
    <button type="button" data-bs-target="#carouselExampleIndicators" data-bs-slide-to="0" class="active" aria-current="true" aria-label="Slide 1"></button>
    <button type="button" data-bs-target="#carouselExampleIndicators" data-bs-slide-to="1" aria-label="Slide 2"></button>
    <button type="button" data-bs-target="#carouselExampleIndicators" data-bs-slide-to="2" aria-label="Slide 3"></button>
	<button type="button" data-bs-target="#carouselExampleIndicators" data-bs-slide-to="3" aria-label="Slide 4"></button>
    <button type="button" data-bs-target="#carouselExampleIndicators" data-bs-slide-to="4" aria-label="Slide 5"></button>
  </div>
  <div class="carousel-inner">
    <div class="carousel-item active">
      <img src="image/1 (1).jpg" class="d-block w-100" alt="...">
    </div>
    <div class="carousel-item">
      <img src="image/1 (2).jpg" class="d-block w-100" alt="...">
    </div>
    <div class="carousel-item">
      <img src="image/1 (3).jpg" class="d-block w-100" alt="...">
    </div>
	<div class="carousel-item">
      <img src="image/1 (4).jpg" class="d-block w-100" alt="...">
    </div>
    <div class="carousel-item">
      <img src="image/9.jpg" class="d-block w-100" alt="...">
    </div>
  </div>
</div>


<p style="color: #012699;"><strong>4. Hệ thống chiêu thức, chiến đấu và khả năng nhân vật:</strong></p>
<p>Mỗi hành tinh có những hệ thống chiêu thức khác nhau, tùy vào sở thích và khả năng bản thân, bạn có thể nâng cấp chiêu thức, cũng như tiềm năng bản thân để đạt sức mạnh cao nhất. Cân bằng hài hòa giữa chỉ số bản thân và chiêu thức có thể giúp bạn rất nhiều trong con đường trở thành chiến binh huyền thoại.</p>
<p>*Xayda có các chiêu thức huyền thoại điển hình như: Biến hình, Tự phát nổ</p>
<p style="text-align:center"><img alt src="../image/2 (2).jpg" style="max-width:100%"></p>
<p>*Trái đất có các chiêu thức huyền thoại điển hình như: Quả cầu kênh khi, Kaioken, Trái dương hạ san, Kamejoko</p>
<p style="text-align:center"><img alt src="../image/2 (1).jpg" style="max-width:100%"></p>
<p>*Namếc có các chiêu thức huyền thoại điển hình như: Makankosappo, Đẻ trứng, Trị thương</p>
<p style="text-align:center"><img alt src="../image/2 (3).jpg" style="max-width:100%"></p>
<p style="color: #012699;"><strong>5. Nhiệm vụ chính tuyến và nhiệm vụ thành tích:</strong></p>
<p>- Game có các nhiệm vụ chính tuyến tiêu diệt quái, đạt sức mạnh, hạ boss, đa dạng phong phú đi theo cốt truyện. Thông qua những nhiệm vụ này, bạn có thể rèn luyện bản thân khỏe mạnh và hoàn thiện hơn</p>
<p style="text-align:center"><img alt src="../image/3 (3).jpg" style="max-width:100%"></p>
<p>- Ngoài ra còn có thêm các nhiệm vụ hàng ngày, từ dễ đến khó, giúp các nhân vật tập luyện, khi hoàn thành các nhân vật có thể được nhận thêm sức mạnh, tiềm năng </p>
<p style="text-align:center"><img alt src="../image/3 (1).jpg" style="max-width:100%"></p>
<p>- Và có thêm hoạt động thành tựu, giúp người chơi có thể kiếm được ngọc hồng trong game thông qua các hoạt động hàng ngày như đạt sức mạnh, đánh quái, online chăm chỉ,…</p>
<p style="text-align:center"><img alt src="../image/3 (2).jpg" style="max-width:100%"></p>
<p style="color: #012699;"><strong>6. Vật phẩm:</strong></p>
<p>- Nhân vật có thể kiếm vật phẩm trang bị cho nhân vật của mình bằng cách tích lũy vàng trong game hoặc đánh quái, hạ Boss rớt ra. </p>
<p style="text-align:center"><img alt src="../image/4 (3).jpg" style="max-width:100%"></p>
<p>- Ngoài ra bạn có thể mua các sách kỹ năng để nâng cấp chiêu thức, cũng như cải trang để biến hóa hình dạng, tạo sự độc nhất cho nhân vật của mình.</p>
<p style="text-align:center"><img alt src="../image/4 (1).jpg" style="max-width:100%"></p>
<p style="text-align:center"><img alt src="../image/4 (2).jpg" style="max-width:100%"></p>
<p style="color: #012699;"><strong>7. Chức năng vật phẩm bay</strong></p>
<p>Với thú cưỡi, các bạn sẽ được phục hồi KI trong khi bay,các vật phẩm bay đặc biệt có thêm các Option đặc biệt để hỗ trợ người chơi tăng KI, HP . Hãy nhanh chóng tìm mua vật phẩm yêu thích và hữu ích trên trong cửa hàng, có thể kiếm tại vòng quay thượng đế khi sự kiện diễn ra</p>
<p style="text-align:center"><img alt src="../image/4.jpg" style="max-width:100%"></p>

<p style="color: #012699;"><strong>8.&nbsp;Đệ Tử</strong></p>
<p>- Xuất hiện 1 boss với tên gọi Broly ban đầu mới xuất hiện sẽ rất yếu. Nhưng khi đánh hắn, hắn sẽ mạnh dần lên đến khi hắn biến hình thành siêu xayda tóc vàng (Super Broly) và sẽ dắt theo 1 đệ tử. Nếu bạn đánh thắng Super Broly bạn sẽ nhận được tên đệ tử ấy.</p>
<p style="text-align:center"><img alt src="../image/4 (5).jpg" style="max-width:100%"></p>
<p>- Đệ Tử Mabư: tại Thành phố vegeta, các nhân vật đi theo Tapion, và tiêu diệt Boss Hirudegarn, khi mà hạ gục Boss sẽ có cơ hội nhận được Quả trứng</p>
<p style="text-align:center"><img alt src="../image/4 (7).jpg" style="max-width:100%"></p>
<p>- Đệ Tử Berus: tại Hành Tinh Berus, các nhân vật đi theo Champa, và tiêu diệt Boss Berus Hoặc Champa, khi mà hạ gục Boss sẽ có cơ hội nhận được Quả trứng hắc ám</p>
<p style="text-align:center"><img alt src="../image/4 (6).jpg" style="max-width:100%"></p>
<p>- Đệ Tử Zeno: tại Hành Tinh Kaio, các nhân vật đi theo Daishinkan., và tiêu diệt Boss Zeno, khi mà hạ gục Boss sẽ có cơ hội nhận được Capsu Gọi Zeno</p>
<p style="text-align:center"><img alt src="../image/4 (4).jpg" style="max-width:100%"></p>
<p style="color: #012699;"><strong>9. Ra mắt các võ đài</strong></p>
Võ đài lần thứ 23:</li>
</ul>
<p>+ Các nhân vật có thể đánh bại các Boss theo thứ tự: Sói héc quyn, Ở dơ, Xinbato, Tàu pảy pảy, khi tham gia đánh bại các Boss các bạn sẽ nhận được phần thưởng rương gỗ tương đương với Boss ở cấp độ đó</p>
<p>+ Ví dụ bạn đánh thắng Sói héc quyn, bạn sẽ nhận được Rương gỗ cấp 1</p>
<p style="text-align:center"><img alt src="../image/5.jpg" style="max-width:100%"></p>

<p style="color: #012699;"><strong>10. Chiêu thức Lưỡng Long Nhất Thể:</strong></p>
<p style="text-align:center"><img alt src="../image/5 (1).jpg" style="max-width:100%"></p>
<p style="text-align:center"><img alt src="../image/5 (2).jpg" style="max-width:100%"></p>
<p>Bao gồm 4 loại:<br>
-&nbsp;Lưỡng long nhất thể: giữ trạng thái biến hình 10 phút, sau khi tách ra phải chờ 10 phút sau mới dùng lại được. (dành cho dân Trái Đất và Xayda)<br>
-&nbsp;Dùng bông tai Porata: ( có bán tại npc Urôn) dùng bông tai thì bất cứ lúc nào cũng hợp thể được và chủ dùng tách ra khi sử dụng lại bông tai, bông tai dùng vĩnh viễn.(dành cho dân Trái Đất và Xayda)<br>
-&nbsp;Hợp thể của người Namek.(dành cho dân Namek)<br>
-&nbsp;Hợp thể vĩnh viễn của người Namek: là đệ tử sẽ mãi mãi mất đi, khi đó toàn bộ sức mạnh của đệ tử sẽ biến thành tiềm năng của sư phụ.(dành cho dân Namek)</p>
<p style="color: #012699;"><strong>11. Trang bị Pha Lê:</strong></p>
<p>● Chức năng pha lê hóa trang bị: Những món đồ thích hợp(vải thô,lưỡng long,jean,zealot, v.v..), hãy đem tới đảo Kame gặp NPC bà hạt mít để được phù phép pha lê hóa cho trang bị của bạn. Những trang bị này sẽ trở nên vip hơn, mạnh hơn khi đã được phù phép. khi phù phép thành công món đồ bạn sẽ có thêm sao xám</p>
<ul>
<li>+ Khi đánh quái sẽ có cơ hội nhận được trang bị pha lê có thể ép pha lê vào</li>
<li>+ Có 9 loại pha lê, 9 màu sắc và tác dụng khác nhau, bạn có thể dùng thêm ngọc rồng để sao pha lê hóa</li>
<li>+ Bạn hãy đến gặp bà hạt mít tại đảo kame để ép pha lê vào trang bị pha lê. Hãy nhớ trước khi ép ngọc NPC bà Hạt Mít sẽ cho biết trước thông tin nhé.</li>
</ul>
<p style="text-align:center"><img alt src="../image/6 (3).jpg" style="max-width:100%"></p>
<p>● Chức năng chuyển hóa trang bị đồ hủy diệt thành đồ kích hoạt:hãy đem tới đảo Kame gặp NPC bà hạt mít để được phù phép</p>
<ul>
<li>+ Chọn 1 trang bị món đồ hủy diệt tương ứng\n(Áo, quần, găng, giày hoặc nhẫn)(Có thể thêm 1 món đồ thần linh bất kỳ để tăng tỉ lệ)! Chỉ cần chọn 'Nâng Cấp'"</li>
</ul>
<p style="text-align:center"><img alt src="../image/6 (2).jpg" style="max-width:100%"></p>

<p>● Chức năng chuyển hóa thiên sứ thành đồ kích hoạt VIP</p>
<ul>
+ Chọn 1 trang bị thiên sứ bất kì Chọn tiếp ngẫu nhiên 2 món SKH thường đồ SKH VIP sẽ cùng loại với đồ thiên sứ! Chỉ cần chọn 'Nâng Cấp'"</li>
</ul>
<p style="text-align:center"><img alt src="../image/6 (1).jpg" style="max-width:100%"></p>

<p style="color: #012699;"><strong>12. Hoạt động phó bản hàng ngày</strong></p>
<p>- Để tham gia các bạn đến gặp Lính canh rừng Bambo trái đất.&nbsp;Có 10 cửa ải : Từ tường thành đến Trại độc nhãn và cuối cùng là Tầng 1 , Tầng 2 , Tầng 3 , Tầng 4</p>
<p style="text-align:center"><img alt src="../image/7 (1).jpg" style="max-width:100%"></p>
<p style="text-align:center"><img alt src="../image/7 (2).jpg" style="max-width:100%"></p>


<p style="color: #012699;"><strong>13. Hệ thống Boss đa dạng</strong></p>

<p>Hệ thống đi theo lối truyện từ các nhân vật phe phản diện gồm các boss: Xên, Fide, Tiểu đội sát thủ, Bộ đôi </p>
<p>Android19,20, giúp người chơi hình dung và trãi nghiệm săn boss đa dạng và rớt nhiều trang bị vật phẩm hiếm </p>
<div id="carouselExampleAutoplaying" class="carousel slide" data-bs-ride="carousel">
  <div class="carousel-inner">
    <div class="carousel-item active">
      <img src="image/8 (1).jpg" class="d-block w-100" alt="...">
    </div>
    <div class="carousel-item">
      <img src="image/8 (2).jpg" class="d-block w-100" alt="...">
    </div>
    <div class="carousel-item">
      <img src="image/1 (5).jpg" class="d-block w-100" alt="...">
    </div>
  </div>
  </button>
</div>

        <div class="border-secondary border-top"></div>
        <div class="container pt-4 pb-4 text-white">
            <div class="row">
                <div class="col">
                    <div class="text-center">
                        <div style="font-size: 13px" class="text-dark">
                            <?php
                        echo $_group;
                        echo $_fanpage;
                        echo $_copyright;
						?>
						</div>
					</div>
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