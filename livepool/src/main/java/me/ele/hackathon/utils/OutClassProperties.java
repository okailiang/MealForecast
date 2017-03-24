package me.ele.hackathon.utils;

import java.beans.BeanInfo;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * 输出一个类的方法和属性
 *
 * @author oukailiang
 * @create 2016-10-28 上午10:04
 */

public class OutClassProperties {
    private static final String SEPARATOR = "\t";

    /**
     * @param srcFilePath
     * @return
     */
    public static void outProperties(String srcFilePath) {
        BufferedReader realBr = null;
        String realline;
        try {

            File realFile = new File(srcFilePath);
            realBr = new BufferedReader(new FileReader(realFile));
            while ((realline = realBr.readLine()) != null) {
                String[] rowArr = realline.split(SEPARATOR);
                int len = rowArr.length;
                for (int i = 0; i < len; i++) {
                    // System.out.println("private String " + removeSemicolon(rowArr[i]) + ";");
                    System.out.print("\"" + removeSemicolon(rowArr[i]) + "_" + i + "\", ");
                }
                break;
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                realBr.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


    /**
     * @param srcFilePath
     * @return
     */
    public static void outGetMethod(String srcFilePath, String classValue) {
        BufferedReader realBr = null;
        String realline;
        try {

            File realFile = new File(srcFilePath);
            realBr = new BufferedReader(new FileReader(realFile));
            while ((realline = realBr.readLine()) != null) {
                String[] rowArr = realline.split(SEPARATOR);
                int len = rowArr.length;
                for (int i = 0; i < len; i++) {
                    //"(rowArr[" + i + "]);" info:sb.append(info.getX());
                    //env: sb.append(info.getHisEcoEnv().getBrand());
                    //rst :  sb.append(info.getHisRstInfo().getAddress_type())
                    System.out.println("sb.append(" + classValue + ".getHisRstInfo()" + ".get" +
                            firstLetterToUpperCase(removeSemicolon(rowArr[i])) + "());");
                }
                break;
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                realBr.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * @param srcFilePath
     * @return
     */
    public static void outSetMethod(String srcFilePath, String classValue) {
        BufferedReader realBr = null;
        String realline;
        try {

            File realFile = new File(srcFilePath);
            realBr = new BufferedReader(new FileReader(realFile));
            while ((realline = realBr.readLine()) != null) {
                String[] rowArr = realline.split(SEPARATOR);
                int len = rowArr.length;
                for (int i = 0; i < len; i++) {
                    System.out.println(classValue + ".set" + firstLetterToUpperCase(removeSemicolon(rowArr[i])) + "(rowArr[" + i + "]);");
                }
                break;
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                realBr.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 首字母转大写
     *
     * @param str
     * @return
     */
    private static String firstLetterToUpperCase(String str) {
        char[] charArr = str.toCharArray();
        charArr[0] -= 32;
        return String.valueOf(charArr);
    }

    /**
     * 去除列值两边的分号
     *
     * @param colValue
     * @return
     */
    public static String removeSemicolon(String colValue) {
        if (colValue.indexOf("\"") == -1) {
            return colValue;
        }
        char[] charArr = colValue.toCharArray();
        return String.valueOf(Arrays.copyOfRange(charArr, 1, charArr.length - 1));
    }

    /**
     * 自省实现map转Java对象
     *
     * @param map       map<String,Object>
     * @param beanClass 要转为的类
     * @return 要转化的对象类
     * @throws Exception
     */
    public static Object mapToObject(Map<String, Object> map, Class<?> beanClass) throws Exception {
        if (map == null)
            return null;

        Object obj = beanClass.newInstance();
        Object mapValue;

        BeanInfo beanInfo = Introspector.getBeanInfo(obj.getClass());
        PropertyDescriptor[] propertyDescriptors = beanInfo.getPropertyDescriptors();
        for (PropertyDescriptor property : propertyDescriptors) {
            Method setter = property.getWriteMethod();
            if (setter != null) {
                mapValue = map.get(property.getName());
                setterValue(property, mapValue, obj);
            }
        }
        return obj;
    }

    /**
     * 给Java对象设置值
     *
     * @param property 属性对象
     * @param mapValue 给属性赋值的值
     * @param object   Java对象
     */
    private static void setterValue(PropertyDescriptor property, Object mapValue, Object object) throws
            InvocationTargetException, IllegalAccessException, ParseException {
        Method setter = property.getWriteMethod();
        if (mapValue == null) {
            setter.invoke(object, mapValue);
            return;
        }

        Class propertyType = property.getPropertyType();
        String type = propertyType.getName();
        String value = mapValue.toString();

        if (type.equals("java.lang.String")) {
            setter.invoke(object, value);
        } else if (type.equals("java.lang.Integer")) {
            setter.invoke(object, Integer.parseInt(value));
        } else if (type.equals("java.lang.Long")) {
            setter.invoke(object, Long.parseLong(value));
        } else if (type.equals("java.math.BigDecimal")) {
            setter.invoke(object, BigDecimal.valueOf(Double.parseDouble(value)));
        } else if (type.equals("java.math.BigInteger")) {
            setter.invoke(object, BigInteger.valueOf(Long.parseLong(value)));
        } else if (type.equals("java.util.Date")) {
            setter.invoke(object, new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(value));
        } else if (type.equals("java.lang.Boolean")) {
            setter.invoke(object, Boolean.valueOf(value));
        } else if (type.equals("java.lang.Float")) {
            setter.invoke(object, Float.parseFloat(value));
        } else if (type.equals("java.lang.Double")) {
            setter.invoke(object, Double.parseDouble(value));
        } else if (type.equals("java.lang.byte[]")) {
            setter.invoke(object, value.getBytes());
        } else {
            setter.invoke(object, value);
        }
    }

    /**
     * 对象转为map
     *
     * @param obj
     * @return
     * @throws Exception
     */
    public static Map<String, Object> objectToMap(Object obj) throws Exception {
        if (obj == null) {
            return null;
        }

        Map<String, Object> map = new HashMap<String, Object>();

        BeanInfo beanInfo = Introspector.getBeanInfo(obj.getClass());
        PropertyDescriptor[] propertyDescriptors = beanInfo.getPropertyDescriptors();
        for (PropertyDescriptor property : propertyDescriptors) {
            String key = property.getName();
            if (key.compareToIgnoreCase("class") == 0) {
                continue;
            }
            Method getter = property.getReadMethod();
            Object value = getter != null ? getter.invoke(obj) : null;
            map.put(key, value);
        }
        return map;
    }

    public static void main(String[] args) {
        //outProperties("/Users/oukailiang/Downloads/hackathon/E_data/data/his_eco_env.txt");
        outGetMethod("/Users/oukailiang/Downloads/hackathon/E_data/data/rst_info.txt", "info");
        //outSetMethod("/Users/oukailiang/Downloads/hackathon/E_data/data/his_order_info.txt", "order");
    }
}
