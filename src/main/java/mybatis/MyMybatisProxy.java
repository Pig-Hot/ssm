package mybatis;

import java.lang.annotation.Annotation;
import java.lang.reflect.*;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import annotation.*;

/**
 * Created by zhuran on 2019/1/18 0018
 */
//动态代理
@SuppressWarnings("unused")
public class MyMybatisProxy implements InvocationHandler {
    // 代理对象
    private Object target;

    public MyMybatisProxy(Object target) {
        this.target = target;
    }

    /**
     * 代理反射执行
     */
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        Object reuslt = null;
        // reuslt = method.invoke(target, args);
        // 判断方法上是否有注解
        Annotation[] annotations = method.getAnnotations();
        if (null != annotations && annotations.length > 0) {
            // Annotation ann = annotations[0];
            // 可能有多个注解
            for (Annotation annotation : annotations) {
                if (annotation instanceof MyInsert) {
                    // 添加
                    reuslt = intsertSql(method, args);
                } else if (annotation instanceof MyDelete) {
                    // 删除
                } else if (annotation instanceof MyUpdate) {
                    // 修改
                } else if (annotation instanceof MySelect) {
                    // 查询
                    reuslt = selectSql(method, args);
                }
            }
        }
        return reuslt;
    }

    /**
     * 查询
     *
     * @param method
     * @param args
     * @return
     */
    private Object selectSql(Method method, Object[] args) {
        // 判断方法上是否存在select注解
        MySelect mySelect = method.getAnnotation(MySelect.class);
        if (null != mySelect) {
            // 获取注解值 sql语句
            String sql = mySelect.value();
            // 判断sql中是否有参数
            // 没有参数
            // 执行sql语句 返回结果 封装成 返回类型
            // 将rse封装成返回对象
            if (sql.indexOf("#") > 0) {
                // 获取参数 封装成Map
                Map<String, Object> parsMap = this.getMethodParsMap(method, args);
                // 替换参数封装jdbc能执行的语句
                // 根据#{string}参数的顺序 排列 pars里面的顺序 封装成list
                List<Object> ListPar = this.getSqlParNameList(sql, parsMap);
                // 将sql语句 中的 #{string} 替换成 ?
                sql = this.setSqlPars(sql);
                return selectQueryForObject(method, JDBCUtils.getInstance().query(sql, ListPar));
            } else
                return selectQueryForObject(method, JDBCUtils.getInstance().query(sql, ""));
        }
        // 返回结果
        return null;
    }

    // 首字母转小写
    public static String toLowerCaseFirstOne(String s) {
        if (Character.isLowerCase(s.charAt(0)))
            return s;
        else
            return (new StringBuilder()).append(Character.toLowerCase(s.charAt(0))).append(s.substring(1)).toString();
    }

    // 首字母转大写
    public static String toUpperCaseFirstOne(String s) {
        if (Character.isUpperCase(s.charAt(0)))
            return s;
        else
            return (new StringBuilder()).append(Character.toUpperCase(s.charAt(0))).append(s.substring(1)).toString();
    }

    /**
     * 插入
     *
     * @param method
     * @param args
     * @return
     */
    public Object intsertSql(Method method, Object[] args) {
        // 判断方法上是否存在insert注解
        MyInsert myInsert = method.getAnnotation(MyInsert.class);
        if (null != myInsert) {
            // 获取注解值 sql语句
            String sql = myInsert.value();
            // 判断sql中是否有参数
            if (sql.indexOf("#") > 0) {
                // 获取参数 封装成Map
                Map<String, Object> parsMap = this.getMethodParsMap(method, args);
                // 替换参数封装jdbc能执行的语句
                // 根据#{string}参数的顺序 排列 pars里面的顺序 封装成list
                List<Object> ListPar = this.getSqlParNameList(sql, parsMap);
                // 将sql语句 中的 #{string} 替换成 ?
                sql = this.setSqlPars(sql);
                // 使用jdbc执行sql语句获取结果
                System.out.println(sql);
                for (Object object : ListPar) {
                    System.out.println("参数：" + object.toString());
                }
                return JDBCUtils.getInstance().insert(sql, ListPar);
            } else {
                // 没有参数
                return JDBCUtils.getInstance().insert(sql);
            }
        }
        // 返回结果
        return false;
    }

    // 截取sql中的参数并封装成map集合
    private List<Object> getSqlParNameList(String sql, Map<String, Object> parsMap) {
        List<Object> ListPar = new ArrayList<Object>();
        // 获取参数
        List<String> listPars = this.getSqlParsList(sql);
        // 获取设置jdbc参数 按顺序
        for (String string : listPars) {
            ListPar.add(parsMap.get(string));
        }
        return ListPar;
    }

    // 获取sql中#{}中的参数名称
    private List<String> getSqlParsList(String sql) {
        List<String> ListPar = new ArrayList<String>();
        if (sql.indexOf("#") > 0) {
            // sql = sql.trim();
            // 获取到}位置
            int indexNext = sql.indexOf("#") + sql.substring(sql.indexOf("#")).indexOf("}");
            // 获取到{位置
            int indexPre = sql.indexOf("#") + sql.substring(sql.indexOf("#")).indexOf("{");
            // 截取#{}中的值
            String parName = sql.substring(indexPre + 1, indexNext);
            ListPar.add(parName.trim());
            sql = sql.substring(indexNext + 1);
            if (sql.indexOf("#") > 0) {
                ListPar.addAll(getSqlParsList(sql));
            }
        }
        return ListPar;
    }

    /**
     * 将sql中替换参数为?
     *
     * @param sql
     * @return
     */
    private String setSqlPars(String sql) {
        List<String> ListPar = new ArrayList<String>();
        if (sql.indexOf("#") > 0) {
            // 获取到}位置
            int indexNext = sql.indexOf("#") + sql.substring(sql.indexOf("#")).indexOf("}");
            // 获取到{位置
            int indexPre = sql.indexOf("#");
            // 截取#{}中的值
            String parName = sql.substring(indexPre, indexNext + 1);
            ListPar.add(parName.trim());
            sql = sql.replace(parName, "?");
            if (sql.indexOf("#") > 0) {
                sql = setSqlPars(sql);
            }
        }
        return sql;
    }


    /**
     * 获取方法里面的参数和param注解中的value值封装成map对象
     */
    private Map<String, Object> getMethodParsMap(Method method, Object[] args) {
        Map<String, Object> parsMap = new HashMap<>();
        Parameter[] parameters = method.getParameters();
        for (int i = 0; i < parameters.length; i++) {
            Parameter parameter = parameters[i];
            // 获取方法参数中是否存在param注解
            MyParam myParam = parameter.getAnnotation(MyParam.class);
            String parName = "";
            // 获取param注解中value的值
            if (null != myParam) {
                parName = myParam.value();
            } else {
                parName = parameter.getName();
            }
            // 将参数名称和参数封装成map集合
            parsMap.put(parName, args[i]);
        }
        return parsMap;
    }

    private Object selectQueryForObject(Method method, List<Map<String, Object>> map) {
        List<Object> listObject = new ArrayList<>();
        try {
            // 判断是否有记录
            if (map.size() == 0) {
                return null;
            }
            String reutrnTypeFlag = "Bean";
            // 将结果封装成方法的返回类型
            Class<?> returnType = method.getReturnType();
            // 判断返回类型为List还是JavaBean
            // 如果为List 再获取List中泛型的值
            Type genericReturnType = method.getGenericReturnType();
            if (genericReturnType instanceof ParameterizedType) {
                ParameterizedType pType = (ParameterizedType) genericReturnType;
                Type rType = pType.getRawType();// 主类型
                Type[] tArgs = pType.getActualTypeArguments();// 泛型类型
                for (int i = 0; i < tArgs.length; i++) {
                    returnType = Class.forName(tArgs[i].getTypeName());
                    reutrnTypeFlag = "List";
                }
            }
            for (Map<String, Object> hashMap : map) {
                Object returnInstance = returnType.newInstance();
                // 获取实例的字段属性
                Field[] declaredFields = returnInstance.getClass().getDeclaredFields();
                hashMap = sqlBean2JavaBean(hashMap);
                for (Field field : declaredFields) {
                    Object value = hashMap.get(field.getName());
                    // 获取set方法对象
                    Method methodSet = returnInstance.getClass().getMethod("set" + toUpperCaseFirstOne(field.getName()), field.getType());
                    field.setAccessible(true);
                    // 为返回结果实例设置值
                    methodSet.invoke(returnInstance, value);
                }
                listObject.add(returnInstance);
            }
            // 如果返还对象为List
            if (reutrnTypeFlag.equals("List")) {
                return listObject;
            } else {
                // 如果返还对象为普通bean
                return listObject.get(0);
            }
        } catch (Exception e) {
            throw new RuntimeException("返回类型反射生成实例失败", e);
        }
    }

    // 获取代理对象
    @SuppressWarnings("unchecked")
    public static <T> T getObjectProxy(Class<T> objSreviceCla) throws Exception {
        MyMybatisProxy invocationHandlerImpl = new MyMybatisProxy(objSreviceCla);
        ClassLoader loader = objSreviceCla.getClassLoader();
        // Class<?>[] interfaces = objSreviceCla.getInterfaces();
        T newProxyInstance = (T) Proxy.newProxyInstance(loader, new Class<?>[]{objSreviceCla},
                invocationHandlerImpl);
        return newProxyInstance;
    }

    //将数据库读取字段转换为Bean字段
    private Map<String, Object> sqlBean2JavaBean(Map<String, Object> hashMap) {
        Map<String, Object> map = new HashMap<>();
        for (String s : hashMap.keySet()) {
            if (s.contains("_")) {
                Object o = hashMap.get(s);
                String[] array = s.split("_");
                StringBuilder sb = new StringBuilder(array[0]);
                if (array.length > 1) {
                    for (int i = 1; i < array.length; i++) {
                        sb.append(toUpperCaseFirstOne(array[i]));
                    }
                }
                map.put(sb.toString(), o);
            } else {
                map.put(s, hashMap.get(s));
            }
        }
        return map;
    }
}
