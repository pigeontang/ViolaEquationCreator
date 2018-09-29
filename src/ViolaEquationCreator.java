/**
 * 软件工程导论结对编程项目——中小学数学卷子自动生成程序
 * @author Yusion Lim & Yuhan Tang;
 */

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.regex.Pattern;

class PreEquation {
    public Random random = new Random();
    //操作数范围
    private int range = 99;
    private boolean isHigh = false;
    public PreEquation(int number, int midNumber, int flag, boolean _isHigh, String[] preEquations, String[] finalEquations) {
        int operatorIndex = 0;
        isHigh = _isHigh;
        for (int i = midNumber; i < midNumber + number; i++) {
            //每道题目的操作数在1-5个之间,则运算符个数应在1-4之间
            //在flag为1时此处的operatorIndex是小学题目运算符的个数
            operatorIndex = random.nextInt(5 - flag) + flag;
            preEquations[i] = preEquationCreator(operatorIndex, operatorIndex == 0);
            //判断重复，如果重复，则重新生成算式
            //只要初步生成的算式不重复，之后添加平方、三角运算等操作后生成的算式也不会重复
            for (int j = 0; j < i; j++) {
                if (preEquations[j].equals(preEquations[i])) {
                    operatorIndex = random.nextInt(5 - flag) + flag;
                    preEquations[i] = preEquationCreator(operatorIndex, operatorIndex == 0);
                    j = 0;
                }
            }
            finalEquations[i] = addOperator(preEquations[i]);
        }
    }
    private String preEquationCreator(int index, boolean isOne) {
        String preEquation = "";
        //生成括号，当只有一个操作数时括号无意义，此时先生成运算符
        if (index > 0) {
            preEquation += randomOperatorCreator();
            index--;
        }
        boolean s = true;
        while (index > 0) {
            int r = random.nextInt(4);
            //等式起始与终结处的括号无意义，已生成括号则不再在同一处生成括号，在乘法或除法左右生成括号无意义，预计每生成三个以上运算符就有一个括号
            if (r < 2 && index > 1 && s && !preEquation.equals("*") && !preEquation.equals("/")) {
                preEquation = '(' + preEquation + ')';
                s = false;
            }
            else if (r < 3) {
                preEquation = randomOperatorCreator() + preEquation;
                s = true;
                index--;
            }
            else {
                preEquation += randomOperatorCreator();
                s = true;
                index--;
            }
        }
        String finalEquation = "";
        //随机操作数
        int randomNumber = 0;
        //只有一个操作数时
        if (isOne) {
            randomNumber = random.nextInt(range) + 1;
            //为了是三角函数可以运算，限制高中题目操作数的范围
            if (isHigh){
                randomNumber = (randomNumber % 5 + 1) * 15;
            }
            preEquation = Integer.toString(randomNumber) + "=";
            finalEquation = preEquation;
        }
        else {
            preEquation += '=';
            for (int i = 0; i < preEquation.length(); i++) {
                //在上面生成的表达式中加入数字
                randomNumber = random.nextInt(range) + 1;
                if (isHigh){
                    randomNumber = (randomNumber % 5 + 1) * 15;
                }
                if (isOperator(preEquation.charAt(i))) {
                    if (i==0) {
                        finalEquation = Integer.toString(randomNumber) + preEquation.charAt(i);
                    }
                    else if (preEquation.charAt(i - 1) == ')') {
                        finalEquation += preEquation.charAt(i);
                    }
                    else {
                        finalEquation += Integer.toString(randomNumber) + preEquation.charAt(i);
                    }
                }
                else if (preEquation.charAt(i) == ')' && isOperator(preEquation.charAt(i - 1))) {
                    finalEquation += Integer.toString(randomNumber) + ")";
                }
                else if (preEquation.charAt(i) == '=' && isOperator(preEquation.charAt(i - 1))) {
                    finalEquation += Integer.toString(randomNumber) + "=";
                }
                else
                    finalEquation += preEquation.charAt(i);
            }
        }
        return finalEquation;
    }
    private boolean isOperator(char ch){
        return ch == '+' || ch == '-' || ch == '*' || ch == '/';
    }
    //生成运算符
    private char randomOperatorCreator() {
        switch (random.nextInt(4)) {
            case 0:
                return '+';
            case 1:
                return '-';
            case 2:
                return '*';
            default:
                return '/';
        }
    }
    public String addOperator(String tmp) {
        return tmp;
    }
}

class JuniorEquation extends PreEquation {
    public JuniorEquation(int number, int midNumber, int flag, boolean isHigh, String[] preEquations, String[] finalEquations) {
        super(number, midNumber, flag, isHigh, preEquations, finalEquations);
    }
    @Override
    public String addOperator(String tmp) {
        StringBuilder sb = new StringBuilder(tmp);
        int r = 0;
        boolean hasOne = false;
        int addCount = 0;
        if (randomJuniorOperatorCreator() == '\u221A') {
            //等式内只有一个操作数
            if (tmp.length() <= 3) {
                sb.insert(findFirstNumberPlace(tmp, true), '\u221A');
            }
            else {
                for (int i = 0; i < tmp.length(); i++) {
                    if (tmp.charAt(i) >= '0' && tmp.charAt(i) <= '9') {
                        if (i == 0 || tmp.charAt(i - 1) < '0' || tmp.charAt(i - 1) > '9') {
                            r = random.nextInt(3);
                            if (r == 0) {
                                sb = sb.insert(i + addCount, '\u221A');
                                addCount++;
                                hasOne = true;
                            }
                        }
                    }
                }
                //从未添加过对号，则查找第一个数字的位置，在其前面插入对号
                if (!hasOne) {
                    sb = sb.insert(findFirstNumberPlace(tmp, true), '\u221A');
                }
            }
        }
        else {
            if (tmp.length() <= 3) {
                sb.insert(findFirstNumberPlace(tmp, false), '\u00B2');
            }
            else {
                for (int i = 0; i < tmp.length(); i++) {
                    if ((tmp.charAt(i) >= '0' && tmp.charAt(i) <= '9') || tmp.charAt(i) == ')') {
                        if (tmp.charAt(i + 1) < '0' || tmp.charAt(i + 1) > '9') {
                            r = random.nextInt(3);
                            if (r == 0) {
                                sb = sb.insert(i + 1 + addCount, '\u00B2');
                                addCount++;
                                hasOne = true;
                            }
                        }
                    }
                }
                //从未添加过平方号，则查找第一个数字的位置，在其后面插入平方号
                if (!hasOne) {
                    sb = sb.insert(findFirstNumberPlace(tmp, false), '\u00B2');
                }
            }
        }
        return sb.toString();
    }
    private char randomJuniorOperatorCreator() {
        switch (random.nextInt(2)) {
            case 0:
                //使用对号来代替根号
                return '\u221A';
            default:
                //平方符号
                return '\u00B2';
        }
    }
    private int findFirstNumberPlace(String tmp, boolean addFlag) {
        for (int i = 0; i < tmp.length(); i++) {
            if (tmp.charAt(i) >= '0' && tmp.charAt(i) <= '9') {
                if (addFlag) {
                    return i;
                }
                else if (tmp.charAt(i + 1) < '0' || tmp.charAt(i + 1) > '9') {
                    return i + 1;
                }
            }
        }
        return 0;
    }
}

class HighEquation extends PreEquation {
    public HighEquation(int number, int midNumber, int flag, boolean isHigh, String[] preEquations, String[] finalEquations) {
        super(number, midNumber, flag, isHigh, preEquations, finalEquations);
    }
    @Override
    public String addOperator(String tmp) {
        StringBuilder sb = new StringBuilder(tmp);
        int r = 0;
        boolean hasOne = false;
        int addCount = 0;
        if (tmp.length() <= 3) {
            sb.insert(findFirstNumberPlace(tmp), randomCircularOperatorCreator());
        }
        else {
            for (int i = 0; i < tmp.length(); i++) {
                if (tmp.charAt(i) >= '0' && tmp.charAt(i) <= '9') {
                    if (i == 0 || tmp.charAt(i - 1) < '0' || tmp.charAt(i - 1) > '9') {
                        r = random.nextInt(3);
                        if (r == 0) {
                            sb = sb.insert(i + addCount, randomCircularOperatorCreator());
                            addCount += 3;
                            hasOne = true;
                        }
                    }
                }
            }
            if (!hasOne) {
                sb = sb.insert(findFirstNumberPlace(tmp), randomCircularOperatorCreator());
            }
        }
        return sb.toString();
    }
    private String randomCircularOperatorCreator() {
        switch (random.nextInt(3)) {
            case 0:
                return "sin";
            case 1:
                return "cos";
            default:
                return "tan";
        }
    }
    private int findFirstNumberPlace(String tmp) {
        for (int i = 0; i < tmp.length(); i++) {
            if (tmp.charAt(i) >= '0' && tmp.charAt(i) <= '9') {
                return i;
            }
        }
        return 0;
    }
}

public class ViolaEquationCreator {
    private static boolean isInteger(String str) {
        Pattern pattern = Pattern.compile("^[-+]?[\\d]*$");
        return pattern.matcher(str).matches();
    }
    public static void main(String[] args) throws IOException {
        Scanner in = new Scanner(System.in);
        System.out.println("请分别输入用户名和密码，两者以空格间隔：");
        //以此变量判断在何难度下
        String status = null;
        //用户名
        String name = null;
        //用户密码
        int password = 0;
        //本次生成的题目数
        int number = 0;
        //总共生成的题目数
        int finalNumber = 0;
        //未因难度不同而添加运算符时生成的题目数组
        String[] preEquations = {};
        //因难度不同而添加运算符时生成的最终题目数组
        String[] finalEquations = {};
        File file;
        BufferedWriter bw;
        //设置时间格式
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss");
        //目录路径
        String dirPath = null;
        //生成的txt文件路径
        String txtPath = null;
        while (status == null) {
            switch (name = in.next()) {
                case "张三1":
                case "张三2":
                case "张三3":
                    status = "小学";
                    break;
                case "李四1":
                case "李四2":
                case "李四3":
                    status = "初中";
                    break;
                case "王五1":
                case "王五2":
                case "王五3":
                    status = "高中";
                    break;
                default:
                    status = null;
                    break;
            }
            password = in.nextInt();
            if (password != 123) {
                status = null;
            }
            if (status == null) {
                System.out.println("请输入正确的用户名、密码：");
            }
        }
        while (true) {
            System.out.println("准备生成" + status + "数学题目，请输入生成题目数量（10-30，不足则取10，多出则取30）：（或输入\"切换为(小学、初中和高中)\"进行题目难度切换，输入\"0\"则退出程序）");
            String tmp = in.next();
            while (!isInteger(tmp)) {
                switch (tmp) {
                    case "切换为小学":
                        status = "小学";
                        System.out.println("准备生成小学数学题目，请输入生成题目数量（10-30，不足则取10，多出则取30）：（或输入\"切换为(小学、初中和高中)\"进行题目难度切换，输入\"0\"则退出程序）");
                        tmp = in.next();
                        break;
                    case "切换为初中":
                        status = "初中";
                        System.out.println("准备生成初中数学题目，请输入生成题目数量（10-30，不足则取10，多出则取30）：（或输入\"切换为(小学、初中和高中)\"进行题目难度切换，输入\"0\"则退出程序）");
                        tmp = in.next();
                        break;
                    case "切换为高中":
                        status = "高中";
                        System.out.println("准备生成高中数学题目，请输入生成题目数量（10-30，不足则取10，多出则取30）：（或输入\"切换为(小学、初中和高中)\"进行题目难度切换，输入\"0\"则退出程序）");
                        tmp = in.next();
                        break;
                    default:
                        System.out.println("输入有误，请输入\"切换为(小学、初中和高中)\"进行切换，输入\"0\"则退出程序：");
                        tmp = in.next();
                        break;
                }
            }
            if (isInteger(tmp)) {
                number = Integer.parseInt(tmp);
                if (number == 0) {
                    break;
                }
                if (number < 10) {
                    number = 10;
                }
                else if (number > 30) {
                    number = 30;
                }
                finalNumber += number;
            }
            preEquations = Arrays.copyOf(preEquations, preEquations.length + number);
            finalEquations = Arrays.copyOf(finalEquations, finalEquations.length + number);
            if (status.equals("小学")) {
                PreEquation p = new PreEquation(number, finalNumber - number, 1, false, preEquations, finalEquations);
            }
            if (status.equals("初中")) {
                JuniorEquation j = new JuniorEquation(number, finalNumber - number, 0, false, preEquations, finalEquations);
            }
            if (status.equals("高中")) {
                HighEquation h = new HighEquation(number, finalNumber - number, 0, true, preEquations, finalEquations);
            }
            dirPath = ".\\" + name;
            file = new File(dirPath);
            if (!file.exists()) {
                file.mkdir();
            }
            txtPath = ".\\" + name + "\\" + sdf.format(new Date()) + ".txt";
            bw = new BufferedWriter(new FileWriter(txtPath));
            //记录序号
            int count = 1;
            for (int i = finalEquations.length - number; i < finalEquations.length; i++) {
                bw.write(count + ". " + finalEquations[i] + "\r\n");
                count++;
            }
            System.out.println("生成成功，已生成该文件。");
            count = 1;
            bw.close();
        }
    }
}
