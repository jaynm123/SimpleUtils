package com.tianer.ch.utils;

import android.content.Context;
import android.text.TextUtils;
import android.widget.TextView;
import android.widget.Toast;

import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.ArrayList;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

/**
 * 校验类
 */
public class SimVation {


    private ArrayList<TextView> listet;
    private Context context;
    /**
     * 默认
     */
    private static final int DEFOULT = 0;
    /**
     * 电话
     */
    private static final int TYPE_TEL = 110;
    private static final String TYPE_TEL_MESSAGE = "请输入正确的手机号码";

    public SimVation(Context context) {
        this.context = context;
        listet = new ArrayList<TextView>();
    }

    /**
     * 添加一个edittext
     *
     * @param et
     */
    public void addEdit(TextView et, String t) {
        if (et == null) {
            return;
        }
        // 设置tag
        Type type = new Type(t, DEFOULT, "");
        et.setTag(type);
        listet.add(et);
    }

    public void addEdit(TextView et, String t, boolean istel) {
        if (et == null) {
            return;
        }
        Type type = new Type(t, TYPE_TEL, TYPE_TEL_MESSAGE);
        et.setTag(type);
        listet.add(et);

    }

    /**
     * 效验
     *
     * @return
     */
    public boolean matchs() {
        if (listet == null || listet.size() == 0) {
            return true;
        }
        for (TextView tv : listet) {
            String text = tv.getText().toString();
            if (text.equals("")) {
                try {
                    Type t = (Type) tv.getTag();
                    showtoast(t.getMessage());
                } catch (Exception e) {
                    return false;
                }

                return false;
            } else {
                try {
                    Type t = (Type) tv.getTag();
                    if (t.getType() == TYPE_TEL) {
                        if (!isMobileNO(text)) {
                            showtoast(t.getTypemessage());
                            return false;
                        }
                    }
                } catch (Exception e) {
                    return false;
                }


            }
        }

        return true;
    }

    /**
     * toast
     *
     * @param t
     */
    private void showtoast(String t) {
        Toast.makeText(context, t, Toast.LENGTH_SHORT).show();
    }


    class Type {
        public Type(String message, int type, String typemessage) {
            this.message = message;
            this.type = type;
            this.typemessage = typemessage;
        }

        /**
         * 为空时,msg
         */
        private String message;
        /**
         * 类型
         */
        private int type;
        /**
         * 类型判断出错,msg
         */
        private String typemessage;

        public String getTypemessage() {
            return typemessage;
        }

        public void setTypemessage(String typemessage) {
            this.typemessage = typemessage;
        }

        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }

        public int getType() {
            return type;
        }

        public void setType(int type) {
            this.type = type;
        }

    }

    /**
     * 验证手机格式
     */
    public static boolean isMobileNO(String mobiles) {
        /*
         * 移动：134、135、136、137、138、139、150、151、157(TD)、158、159、187、188
		 * 联通：130、131、132、152、155、156、185、186 电信：133、153、180、189、（1349卫通）
		 * 总结起来就是第一位必定为1，第二位必定为3或5或8，其他位置的可以为0-9
		 * "[1]"代表第1位为数字1，"[35789]"代表第二位可以为3、5、7、8、9中的一个，"\\d{9}"代表后面是可以是0～9的数字，
		 * 有9位。
		 */
        String telRegex = "[1][35789]\\d{9}";
        if (TextUtils.isEmpty(mobiles))
            return false;
        else
            return mobiles.matches(telRegex);
    }

    /**
     * @param text  待效验文字
     * @param regex 中文字符 [\u4e00-\u9fa5]
     *              email [\w!#$%&'*+/=?^_`{|}~-]+(?:\.[\w!#$%&'*+/=?^_`{|}~-]+)*@(?:[\w](?:[\w-]*[\w])?\.)+[\w](?:[\w-]*[\w])?
     *              邮政编码 [1-9]\d{5}(?!\d)
     *              身份证号 ^(\d{6})(\d{4})(\d{2})(\d{2})(\d{3})([0-9]|X)$
     *              正整数 ^[1-9]\d*$
     *              整数 ^-?[1-9]\d*$
     *              正浮点数 ^[1-9]\d*\.\d*|0\.\d*[1-9]\d*$
     *              负浮点数 ^-[1-9]\d*\.\d*|-0\.\d*[1-9]\d*$
     *              网址 ^((https|http|ftp|rtsp|mms)?://)[^s]+
     * @return
     */
    public static boolean maths(String text, String regex) {
        if (TextUtils.isEmpty(text)) {
            return false;
        }

        return text.matches(regex);
    }

    /**
     * 加密
     *
     * @param content  需要加密的内容
     * @param password 加密密码
     * @return
     */
    public static String encrypt(String content, String password) {
        try {
            KeyGenerator kgen = KeyGenerator.getInstance("AES");
            kgen.init(128, new SecureRandom(password.getBytes()));
            SecretKey secretKey = kgen.generateKey();
            byte[] enCodeFormat = secretKey.getEncoded();
            SecretKeySpec key = new SecretKeySpec(enCodeFormat, "AES");
            Cipher cipher = Cipher.getInstance("AES");// 创建密码器
            byte[] byteContent = content.getBytes("utf-8");
            cipher.init(Cipher.ENCRYPT_MODE, key);// 初始化
            byte[] result = cipher.doFinal(byteContent);
            return parseByte2HexStr(result); // 加密
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (NoSuchPaddingException e) {
            e.printStackTrace();
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (IllegalBlockSizeException e) {
            e.printStackTrace();
        } catch (BadPaddingException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 解密
     *
     * @param content  待解密内容
     * @param password 解密密钥
     * @return
     */
    public static String decrypt(String content, String password) {
        try {
            KeyGenerator kgen = KeyGenerator.getInstance("AES");
            kgen.init(128, new SecureRandom(password.getBytes()));
            SecretKey secretKey = kgen.generateKey();
            byte[] enCodeFormat = secretKey.getEncoded();
            SecretKeySpec key = new SecretKeySpec(enCodeFormat, "AES");
            Cipher cipher = Cipher.getInstance("AES");// 创建密码器
            cipher.init(Cipher.DECRYPT_MODE, key);// 初始化
            byte[] result = cipher.doFinal(content.getBytes("utf-8"));
            return parseByte2HexStr(result); // 加密
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (NoSuchPaddingException e) {
            e.printStackTrace();
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        } catch (IllegalBlockSizeException e) {
            e.printStackTrace();
        } catch (BadPaddingException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 将二进制转换成16进制
     *
     * @param buf
     * @return
     */
    public static String parseByte2HexStr(byte buf[]) {
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < buf.length; i++) {
            String hex = Integer.toHexString(buf[i] & 0xFF);
            if (hex.length() == 1) {
                hex = '0' + hex;
            }
            sb.append(hex.toUpperCase());
        }
        return sb.toString();
    }

    /**
     * 将16进制转换为二进制
     *
     * @param hexStr
     * @return
     */
    public static byte[] parseHexStr2Byte(String hexStr) {
        if (hexStr.length() < 1)
            return null;
        byte[] result = new byte[hexStr.length() / 2];
        for (int i = 0; i < hexStr.length() / 2; i++) {
            int high = Integer.parseInt(hexStr.substring(i * 2, i * 2 + 1), 16);
            int low = Integer.parseInt(hexStr.substring(i * 2 + 1, i * 2 + 2), 16);
            result[i] = (byte) (high * 16 + low);
        }
        return result;
    }
}
