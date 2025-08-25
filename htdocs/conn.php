
	 <?php
        $username = "root"; // Khai báo username
        $password = "";      // Khai báo password
        $server   = "localhost";   // Khai báo server
        $dbname   = "nro";      // Khai báo database


        $connect = new mysqli($server, $username, $password, $dbname);
        if ($connect->connect_error) {
            die("Không kết nối :" . $connect->connect_error);
        }
        ?>
