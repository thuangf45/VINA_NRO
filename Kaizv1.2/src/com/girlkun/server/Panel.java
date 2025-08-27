package com.girlkun.server;

import com.girlkun.utils.Logger;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.*;

/**
 * Lớp Panel cung cấp giao diện điều khiển quản trị server game thông qua các nút chức năng.
 * Lớp này kế thừa từ JPanel và triển khai ActionListener để xử lý các sự kiện từ nút bấm.
 * Các chức năng bao gồm kích hoạt bảo trì server và thay đổi tỷ lệ kinh nghiệm (EXP).
 * 
 * @author Lucifer
 */
public class Panel extends JPanel implements ActionListener {

    /**
     * Nút bấm để kích hoạt chế độ bảo trì server.
     */
    private JButton baotri;

    /**
     * Nút bấm để thay đổi tỷ lệ kinh nghiệm (EXP) của server.
     */
    private JButton thaydoiexp;

    /**
     * Nút bấm để thay đổi sự kiện (chưa được sử dụng trong mã hiện tại).
     */
    private JButton thaydoisk;

    /**
     * Panel chứa các nút bấm.
     */
    private JPanel addPanel;

    /**
     * Khởi tạo một đối tượng Panel với giao diện chứa các nút chức năng.
     * Thêm các nút bấm cho bảo trì và thay đổi EXP vào panel.
     */
    public Panel() {
        JPanel btri = new JPanel();
        add(btri);
        baotri = new JButton("Bảo trì");
        baotri.addActionListener(this);
        addPanel = new JPanel();
        addPanel.add(baotri);
        btri.add(addPanel);

        JPanel exp = new JPanel();
        add(exp);
        thaydoiexp = new JButton("Thay đổi exp");
        thaydoiexp.addActionListener(this);
        addPanel = new JPanel();
        addPanel.add(thaydoiexp);
        exp.add(addPanel);
    }

    /**
     * Xử lý các sự kiện khi người dùng nhấn vào nút bấm.
     * Kích hoạt bảo trì server hoặc thay đổi tỷ lệ EXP dựa trên nút được nhấn.
     * 
     * @param e Sự kiện hành động từ nút bấm.
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == baotri) {
            Maintenance.gI().start(15);
            Logger.error("Tiến Hành Bảo Trì \n");
        } else if (e.getSource() == thaydoiexp) {
            String exp = JOptionPane.showInputDialog(this, "Nhập Exp muốn thay đổi\n" +
                    "EXP hiện tại là :" + Manager.RATE_EXP_SERVER);
            if (exp != null) {
                Manager.RATE_EXP_SERVER = Byte.parseByte(exp);
                Logger.error("EXP hiện tại là :" + exp);
            }
        }
    }
}