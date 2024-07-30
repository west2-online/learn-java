package bonus;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 邮箱格式验证规则：
 * 邮箱的本地部分（@之前的部分）可以包含字母、数字、下划线、点、减号。
 * 域名部分（@之后的部分）包含字母、数字、点和减号。
 */

public class BonusProb2 {
    public static boolean isValidEmail(String email) {
        if (email == null || email.isEmpty()) {
            return false;
        }

        // 定义邮箱格式的正则表达式
        String emailRegex = "^[\\w!#$%&'*+/=?`{|}~^-]+(?:\\.[\\w!#$%&'*+/=?`{|}~^-]+)*@(?:[A-Z0-9](?:[A-Z0-9-]{0,61}[A-Z0-9])?\\.)+[A-Z]{2,6}$";

        // 创建一个Pattern对象
        Pattern pattern = Pattern.compile(emailRegex, Pattern.CASE_INSENSITIVE);

        // 使用Pattern对象创建一个Matcher对象
        Matcher matcher = pattern.matcher(email);

        // 使用Matcher对象的matches方法进行匹配
        return matcher.matches();
    }

    public static void main(String[] args) {
        // 测试不同的邮箱地址
        String[] testEmails = {
                "test@example.com",
                "user.name+tag+sorting@example.com",
                "user@subdomain.example.com",
                "user@localserver",
                "user@domain.c",
                "invalid-email",
                "@missinglocalpart.com",
                "username@.missingtld"
        };

        for (String email : testEmails) {
            System.out.println(email + (isValidEmail(email) ? " ：合法" : " ：不合法"));
        }
    }
}
