package com.lqlsoftware.wxPublic.controller;


import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.lqlsoftware.wxPublic.dao.DBManager;
import com.lqlsoftware.wxPublic.entity.Message;
import com.lqlsoftware.wxPublic.utils.CheckUtil;
import com.lqlsoftware.wxPublic.utils.MsgUtil;
import org.dom4j.DocumentException;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import java.util.Map;

/**
 *
 * Controller
 *
 * @author Robin Lu
 *
 */

@Controller
public class HandleController {

    final String serverName = "gh_92bc3980c793";

    // 微信登陆
    @RequestMapping(value = "/wx")
    public void wxPublic(HttpServletRequest request, HttpServletResponse response)
            throws Exception {

        // 设置传输数据格式
        request.setCharacterEncoding("UTF-8");
        response.setHeader("content-type", "xml;charset=UTF-8");

        String echostr = request.getParameter("echostr");

        // 初次访问进行身份验证
        if (echostr != null && echostr != "") {
            String signature = request.getParameter("signature");
            String timestamp = request.getParameter("timestamp");
            String nonce = request.getParameter("nonce");

            if (CheckUtil.checkSignature(signature, timestamp, nonce)) {
                response.getWriter().write(echostr);
                System.out.println("微信公众号接入成功");
            } else {
                System.out.println("接入失败");
            }
        }
        // 读取xml
        else try {
            Map<String, String> map = MsgUtil.xmlToMap(request);
            String msgType = map.get("MsgType");
            String message = null;

            // 文本类信息
            if (msgType.equals("text")) {
                String toUserName = map.get("ToUserName");
                String fromUserName = map.get("FromUserName");
                String content = map.get("Content");

                System.out.println(fromUserName + " --> " + content);

                String sql = "SELECT * FROM student WHERE name=? OR id=?";
                try (Connection conn = DBManager.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
                    ps.setString(1, content);
                    ps.setString(2, content);
                    ResultSet rs = ps.executeQuery();
                    if (rs.next()) {
                        Message text = new Message();
                        text.setToUserName(fromUserName);
                        text.setFromUserName(toUserName);
                        text.setCreateTime(new Date().getTime());
                        text.setMsgType("text");
                        text.setContent("学号-->" + rs.getString(1) + "\n" +
                                "班级-->" + rs.getString(3) + "\n" +
                                "姓名-->" + rs.getString(2));
                        message = MsgUtil.MessageToXML(text);
                    } else {
                        Message text = new Message();
                        text.setToUserName(fromUserName);
                        text.setFromUserName(toUserName);
                        text.setCreateTime(new Date().getTime());
                        text.setMsgType("text");
                        text.setContent(content + "没有上这堂课哦~");
                        message = MsgUtil.MessageToXML(text);
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                    message = "success";
                }
            }
            // 音频类信息
            else if (msgType.equals("voice")) {
                String toUserName = map.get("ToUserName");
                String fromUserName = map.get("FromUserName");
                String recognition = map.get("Recognition");
                String result = recognition.substring(0, recognition.length() - 1);
                System.out.println(fromUserName + " --> " + result);

                String sql = "SELECT * FROM student WHERE name=? OR id=?";
                try (Connection conn = DBManager.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
                    ps.setString(1, result);
                    ps.setString(2, result);
                    ResultSet rs = ps.executeQuery();
                    if (rs.next()) {
                        Message text = new Message();
                        text.setToUserName(fromUserName);
                        text.setFromUserName(toUserName);
                        text.setCreateTime(new Date().getTime());
                        text.setMsgType("text");
                        text.setContent("学号-->" + rs.getString(1) + "\n" +
                                "班级-->" + rs.getString(3) + "\n" +
                                "姓名-->" + rs.getString(2));
                        message = MsgUtil.MessageToXML(text);
                    } else {
                        Message text = new Message();
                        text.setToUserName(fromUserName);
                        text.setFromUserName(toUserName);
                        text.setCreateTime(new Date().getTime());
                        text.setMsgType("text");
                        text.setContent(result + "没有上这堂课哦~");
                        message = MsgUtil.MessageToXML(text);
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                    message = "success";
                }
            }
            response.getWriter().write(message);
        } catch (DocumentException e) {
            e.printStackTrace();
            response.getWriter().write("success");
        }

    }
}