package com.webSocket.demo.util;

import org.springframework.core.io.ClassPathResource;
import org.springframework.util.StringUtils;

import java.io.*;
import java.util.*;

/**
 * 敏感字符过滤DFA算法demo实现
 */
public class DFADemo {

    private static Map<Object, Object> sensitiveWordMap = null;

    /**
     * 获取敏感词map
     * @param keyWordSet
     * @return
     */
     public static Map<Object, Object> getSensitiveWordMap(Set<String> keyWordSet){
        Map<Object, Object> sensitiveWordMap = new HashMap<>(keyWordSet.size());
        String keyWord = null;
        Map<Object, Object> nowMap = null;
        Map<Object, Object> newMap = null;
        Iterator<String> iterator = keyWordSet.iterator();
        while (iterator.hasNext()){
            keyWord = iterator.next();
            nowMap = sensitiveWordMap;
            for(int i = 0; i < keyWord.length(); i++){
                char keyWordChar = keyWord.charAt(i);
                Object mapObj = nowMap.get(keyWordChar);
                if(mapObj != null){
                    nowMap = (Map) mapObj;
                    if(i != 0 && (Integer) nowMap.get("isEnd") == 1){
                        nowMap.put("isEnd", 2);
                    }
                    continue;
                }else{
                    newMap = new HashMap<>();
                    if (nowMap.get("isEnd") == null) {
                        nowMap.put("isEnd", 0);
                    }
                    nowMap.put(keyWordChar, newMap);
                    nowMap = newMap;
                }
                if(i == keyWord.length() - 1){
                    nowMap.put("isEnd", 1);
                }
            }
        }
         DFADemo.sensitiveWordMap = sensitiveWordMap;
        return sensitiveWordMap;
    }

    /**
     * 获取文字中的敏感词
     * @param txt 文字
     * @param matchType 匹配规则&nbsp;1：最小匹配规则，2：最大匹配规则
     * @return
     * @version 1.0
     */
    public static Set<String> getSensitiveWord(String txt , int matchType){
        Set<String> sensitiveWordList = new HashSet<String>();

        for(int i = 0 ; i < txt.length() ; i++){
            int length = CheckSensitiveWord(txt, i, matchType);    //判断是否包含敏感字符
            if(length > 0){    //存在,加入list中
                sensitiveWordList.add(txt.substring(i, i+length));
                i = i + length - 1;    //减1的原因，是因为for会自增
            }
        }

        return sensitiveWordList;
    }

    /**
     * 检查文字中是否包含敏感字符，检查规则如下：<br>
     * @param txt
     * @param beginIndex
     * @param matchType
     * @return，如果存在，则返回敏感词字符的长度，不存在返回0
     * @version 1.0
     */
    @SuppressWarnings({ "rawtypes"})
    public static int CheckSensitiveWord(String txt,int beginIndex,int matchType){
        boolean  flag = false;    //敏感词结束标识位：用于敏感词只有1位的情况
        int matchFlag = 0;     //匹配标识数默认为0
        char word = 0;
        Map nowMap = sensitiveWordMap;
        for(int i = beginIndex; i < txt.length() ; i++){
            word = txt.charAt(i);
            nowMap = (Map) nowMap.get(word);     //获取指定key
            if(nowMap != null){     //存在，则判断是否为最后一个
                matchFlag++;     //找到相应key，匹配标识+1
                if("1".equals(String.valueOf(nowMap.get("isEnd"))) || "2".equals(String.valueOf(nowMap.get("isEnd")))){       //如果为最后一个匹配规则,结束循环，返回匹配标识数
                    flag = true;       //结束标志位为true
                    if(1 == matchType){    //最小规则，直接返回,最大规则还需继续查找
                        break;
                    }
                }
            }
            else{     //不存在，直接返回
                break;
            }
        }
        if(matchFlag < 2 || !flag){        //长度必须大于等于1，为词
            matchFlag = 0;
        }
        return matchFlag;
    }

    /**
     * 替换敏感字符
     * @param txt 要替换敏感词的字符串
     * @param matchType 匹配规则
     * @param relaceCharter 替换的字符
     * @return
     */
    public static String replaceSensitiveWord(String txt, int matchType, String relaceCharter){
        String word = null;
        //获取字符串中的敏感词
        Set<String> sensitiveSet = getSensitiveWord(txt, matchType);
        Iterator<String> iterator = sensitiveSet.iterator();
        while (iterator.hasNext()) {
            word = iterator.next();
            //获取敏感词对应的替换字符
            String wordRelaceStr = getRelaceChar(relaceCharter, word.length());
            txt = txt.replaceAll(word, wordRelaceStr);
        }
        return txt;
    }

    /**
     * 获取敏感词对应的替换字符
     * @param relaceCharter 替换字符
     * @param length 关键字长度
     * @return 与关键词长度一致的替换字符
     */
    public static String getRelaceChar(String relaceCharter, int length){
        StringBuilder resultBuilder = new StringBuilder();
        for(int i = 0; i < length; i++){
            resultBuilder.append(relaceCharter);
        }
        return resultBuilder.toString();
    }

    public static Set<String> readSensitiveWordFile() throws IOException {
        Set<String> sensitiveWordSet = new HashSet<String>();
        StringBuilder builder = new StringBuilder();
//        ClassPathResource classPathResource = new ClassPathResource("SensitiveWord/SensitiveWord.txt");
//        InputStream inputStream = classPathResource.getInputStream();
        InputStreamReader inputStreamReader = new InputStreamReader(new FileInputStream(new File("D:\\SensitiveWord.txt")), "utf8");
        BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
        String str = null;
        while ((str = bufferedReader.readLine()) != null) {
            builder.append(str);
        }
        System.out.println(builder.toString());
        if(!StringUtils.isEmpty(builder.toString().trim())){
            sensitiveWordSet.addAll(Arrays.asList(builder.toString().trim().split(",")));
        }
        return sensitiveWordSet;
    }

    public static void main(String[] args) throws IOException {
        long startTime = System.currentTimeMillis();
        System.out.println("开始时间:" + startTime);

        Set<String> sensitiveWordSet = new TreeSet<>();
        sensitiveWordSet.add("王八");
        sensitiveWordSet.add("王八蛋");
        sensitiveWordSet.add("王八蛋蛋");
        sensitiveWordSet.add("王八蛋蛋狗");
        sensitiveWordSet.add("LCK");
        Map map = getSensitiveWordMap(sensitiveWordSet);
        System.out.println(map.toString());

        String string = "这是一个王八,嘿嘿王八蛋蛋狗,这是一个王八,嘿嘿王八蛋蛋,这是一个王八,嘿嘿王八蛋蛋，这是一个王八,嘿嘿王八蛋蛋，" +
                "这是一个王八,嘿嘿王八蛋蛋这是一个王八,嘿嘿王八蛋蛋这是一个王八,嘿嘿王八蛋蛋，这是一个王八,嘿嘿王八蛋蛋，，v这是一个王八,嘿" +
                "嘿王八蛋蛋,这是一个王八,嘿嘿王八蛋蛋,这是一个王八,嘿嘿王八蛋蛋，这是一个王八,嘿嘿王八蛋蛋" +
                "这是一个王八,嘿嘿王八蛋蛋这是一个王八,嘿嘿王八截止今日，韩服排行榜前十中有八个账号是LCK的S9出征队伍选手。" +
                "其中GRF的Viper和DWG的ShowMaker更是各有两个账号在榜上。另外，SKT只有Clid一人上榜，其余皆为GRF和DWG的新人选手，" +
                "值得一体的是，前十名中像Viper、ShowMaker和Canyon这样的选手都是00后选手。这是一个王八,嘿嘿王八蛋蛋,这是一个王八,嘿嘿王八蛋蛋,这是一个王八,嘿嘿王八蛋蛋，这是一个王八,嘿嘿王八蛋蛋，\" +\n" +
                "                \"这是一个王八,嘿嘿王八蛋蛋这是一个王八,嘿嘿王八蛋蛋这是一个王八,嘿嘿王八蛋蛋，这是一个王八,嘿嘿王八蛋蛋，，v这是一个王八,嘿\" +\n" +
                "                \"嘿王八蛋蛋,这是一个王八,嘿嘿王八蛋蛋,这是一个王八,嘿嘿王八蛋蛋，这是一个王八,嘿嘿王八蛋蛋\" +\n" +
                "                \"这是一个王八,嘿嘿王八蛋蛋这是一个王八,嘿嘿王八截止今日，韩服排行榜前十中有八个账号是LCK的S9出征队伍选手。\" +\n" +
                "                \"其中GRF的Viper和DWG的ShowMaker更是各有两个账号在榜上。另外，SKT只有Clid一人上榜，其余皆为GRF和DWG的新人选手，\" +\n" +
                "                \"值得一体的是，前十名中像Viper、ShowMaker和Canyon这样的选手都是00后选手。";
        Set<String> set = getSensitiveWord(string, 2);
        System.out.println(set.toString());

        String txt = replaceSensitiveWord(string, 2, "*");
        System.out.println(txt);

        long endTime = System.currentTimeMillis();
        System.out.println("结束时间:" + endTime);
        System.out.println("用时:" + (endTime - startTime));

//        Set<String> sensitivwSet = readSensitiveWordFile();
//        System.out.println(sensitivwSet);
    }

}
