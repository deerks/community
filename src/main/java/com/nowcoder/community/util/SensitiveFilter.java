package com.nowcoder.community.util;

import com.nowcoder.community.controller.interceptor.LoginRequiredInterceptor;
import io.micrometer.common.util.StringUtils;
import jakarta.annotation.PostConstruct;
import org.apache.commons.lang3.CharUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

@Component
public class SensitiveFilter {

    private static final Logger logger = LoggerFactory.getLogger(SensitiveFilter.class);

    //定义替换符
    private static final String REPLACEMENT = "***";

    //根节点
    private TrieNode rootNode = new TrieNode();

    //服务启动之前初始化
    @PostConstruct
    public void init() {
        try (
                InputStream is = this.getClass().getClassLoader().getResourceAsStream("sensitive-words.txt");
                BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        ) {
            String keyword;
            while((keyword = reader.readLine()) != null) {
                //添加到前缀树
                this.addKeyword(keyword);

            }
        }catch (IOException e){
            logger.error("加载敏感词文件失败"+ e.getMessage());
        }

    }

    //将敏感此添加到前缀树的方法
    private void addKeyword(String keyword) {
        TrieNode tempNode = rootNode;
        for (int i = 0; i < keyword.length(); i++) {
            char c = keyword.charAt(i);
            TrieNode subNode = tempNode.getSubNode(c);
            if (subNode == null) {
                //初始化子节点
                subNode = new TrieNode();
                tempNode.addSubNode(c, subNode);
            }
            //进入下一轮循环
            tempNode = subNode;

            //标记结束位置
            if (i == keyword.length() - 1) {
                tempNode.setKeyWordEnd(true);
            }
        }
    }

    /**
     * 返回过滤后的字符串
     */
    public String filter(String text) {
        if (StringUtils.isBlank(text)) {
            return null;
        }
        //声明三个指针
        TrieNode tempNode = rootNode;
        int begin = 0, position = 0;

        //记录结果
        StringBuilder sb = new StringBuilder();

        while (position < text.length()) {
            char c = text.charAt(position);
            //跳过特殊符号
            if (isSymbol(c)) {
                //节点指针处于根节点
                if(tempNode == rootNode) {
                    sb.append(c);
                    begin++;
                }
                position++;
                continue;
            }
            //检查下级节点
            tempNode = tempNode.getSubNode(c);
            if (tempNode == null) {
                sb.append(text.charAt(begin));
                begin++;
                position = begin;
                tempNode = rootNode;
            }else if (tempNode.isKeyWordEnd) {
                //发现敏感词，需要替换
                sb.append(REPLACEMENT);
                position++;
                begin = position;
                tempNode = rootNode;
            }else {
                //继续检查下一个字符
                position++;
            }
        }
        //记录最后一部分字符
        sb.append(text.substring(begin));
        return sb.toString();
    }

    //判断是否为特殊符号
    private boolean isSymbol(Character c) {
        return !CharUtils.isAsciiAlphanumeric(c) && (c < 0x2E80 || c > 0x9FFF);
    }

    //内部类，定义前缀树节点
    private class TrieNode{

        //关键词结束标识
        private boolean isKeyWordEnd = false;

        //子节点，下级字符和下级节点
        private Map<Character, TrieNode> subNodes = new HashMap<>();

        public boolean isKeyWordEnd() {
            return isKeyWordEnd;
        }

        public void setKeyWordEnd(boolean keyWordEnd) {
            isKeyWordEnd = keyWordEnd;
        }

        //添加子节点
        public void addSubNode(Character c, TrieNode node) {
            subNodes.put(c, node);
        }

        //获取子节点
        public TrieNode getSubNode(Character c){
            return subNodes.getOrDefault(c, null);
        }
    }
}
