package com.rs;

import java.sql.Time;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Clock;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.OffsetDateTime;
import java.time.Period;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

import org.junit.Test;

/**
 * @author rangyushuang <rangyushuang@kuaishou.com>
 * Created on 2022-07-18
 */
public class DateTest {

    @Test
    public void test1() {
        Date currDate = new Date();
        System.out.println(currDate.toString());  // 标准的UTC时间
        // 已经@Deprecated
        System.out.println(currDate.toLocaleString());  // 本地时间 根据本地时区显示的时间格式
        // 已经@Deprecated
        System.out.println(currDate.toGMTString());  // 格林威治GTM时间
    }

    @Test
    public void test2() {
        String patternStr = "yyyy-MM-dd HH:mm:ss";
        // 北京时间（new出来就是默认时区的时间）
        Date bjDate = new Date();

        // 得到纽约的时区
        TimeZone newYorkTimeZone = TimeZone.getTimeZone("America/New_York");
        // 根据此时区 将北京时间转换为纽约的Date
        DateFormat newYorkDateFormat = new SimpleDateFormat(patternStr);
        newYorkDateFormat.setTimeZone(newYorkTimeZone);
        System.out.println("这是北京时间：" + new SimpleDateFormat(patternStr).format(bjDate));
        System.out.println("这是纽约时间：" + newYorkDateFormat.format(bjDate));  // 北京比纽约快13个小时没毛病。
    }

    /**
     * 把所有可用的zoneId打印出来
     */
    @Test
    public void test3() {
        String[] availableIDs = TimeZone.getAvailableIDs();
        System.out.println("可用zoneId总数：" + availableIDs.length);
        for (String zoneId : availableIDs) {
            System.out.println(zoneId);
        }
    }

    @Test
    public void test4() {
        System.out.println(TimeZone.getTimeZone("GMT+08:00").getID());  // 这里只能用GMT+08:00，而不能用UTC+08:00
        System.out.println(TimeZone.getDefault().getID());

        // 纽约时间
        System.out.println(TimeZone.getTimeZone("GMT-05:00").getID());
        System.out.println(TimeZone.getTimeZone("America/New_York").getID());
    }

    /**
     * Java让我们有多种方式可以手动设置/修改默认时区：
     *
     * API方式：强制将时区设为北京时区TimeZone.setDefault(TimeZone.getDefault().getTimeZone("GMT+8"));
     * JVM参数方式：-Duser.timezone=GMT+8
     * 运维设置方式：将操作系统主机时区设置为北京时区，这是推荐方式，可以完全对开发者无感，也方便了运维统一管理
     */


    /**
     * 夏令时是一个“非常烦人”的东西，大大的增加了日期时间处理的复杂度。
     * 比如这个灵魂拷问：若你的出生日期是1988-09-11 00:00:00（夏令时最后一天）且存进了数据库，想一想，对此日期的格式化有没有可能就会出问题呢，
     * 有没有可能被你格式化成1988-09-10 23:00:00呢？
     *
     * 这段代码，在不同的JDK版本下运行，可能出现不同的结果.
     * 总的来说，只要你使用的是较新版本的JDK，开发者是无需关心夏令时问题的，
     * 即使全球仍有很多国家在使用夏令时，咱们只需要面向时区做时间转换就没问题。
     */
    @Test
    public void test5() throws ParseException {
        String patterStr = "yyyy-MM-dd";
        DateFormat dateFormat = new SimpleDateFormat(patterStr);

        String birthdayStr = "1988-09-11";
        // 字符串 -> Date -> 字符串
        Date birthday = dateFormat.parse(birthdayStr);
        long birthdayTimestamp = birthday.getTime();
        System.out.println("老王的生日是：" + birthday);
        System.out.println("老王的生日的时间戳是：" + birthdayTimestamp);

        System.out.println("==============程序经过一番周转，我的同时 方法入参传来了生日的时间戳=============");
        // 字符串 -> Date -> 时间戳 -> Date -> 字符串
        birthday = new Date(birthdayTimestamp);
        System.out.println("老王的生日是：" + birthday);
        System.out.println("老王的生日的时间戳是：" + dateFormat.format(birthday));
    }


    /**
     * Date对象里存的是自格林威治时间（ GMT）1970年1月1日0点至Date所表示时刻所经过的毫秒数，是个数值
     */
    @Test
    public void test6() {
        String patterStr = "yyyy-MM-dd HH:mm:ss";
        Date currDate = new Date(System.currentTimeMillis());

        // 北京时区
        DateFormat bjDateFormat = new SimpleDateFormat(patterStr);
        bjDateFormat.setTimeZone(TimeZone.getDefault());
        // 纽约时区
        DateFormat newYorkDateFormat = new SimpleDateFormat(patterStr);
        newYorkDateFormat.setTimeZone(TimeZone.getTimeZone("America/New_York"));
        // 伦敦时区
        DateFormat londonDateFormat = new SimpleDateFormat(patterStr);
        londonDateFormat.setTimeZone(TimeZone.getTimeZone("Europe/London"));

        // 同一个毫秒值，根据时区/偏移量的不同可以展示多地的时间，这就证明了Date它的时区无关性。
        System.out.println("毫秒数:" + currDate.getTime() + ", 北京本地时间:" + bjDateFormat.format(currDate));
        System.out.println("毫秒数:" + currDate.getTime() + ", 纽约本地时间:" + newYorkDateFormat.format(currDate));
        System.out.println("毫秒数:" + currDate.getTime() + ", 伦敦本地时间:" + londonDateFormat.format(currDate));
    }


    /**
     * 对于格式化器来讲，虽然说编程过程中一般情况下我们并不需要给DateFormat设置时区（那就用默认时区呗）就可正常转换。
     * 但是你必须知道这是由于交互双发默认有个相同时区的契约存在。
     */
    @Test
    public void test7() throws ParseException {
        String patterStr = "yyyy-MM-dd HH:mm:ss";

        // 模拟请求参数的时间字符串
        String dateStrParam = "2020-01-15 18:00:00";

        // 模拟服务端对此服务换转换为Date类型
        DateFormat dateFormat = new SimpleDateFormat(patterStr);
        System.out.println("格式化器用的时区是：" + dateFormat.getTimeZone().getID());
        Date date = dateFormat.parse(dateStrParam);
        System.out.println(date);
    }

    /**
     * Y:当前周所在的年份（不建议使用，周若跨年有坑）
     * w:年中的周数(1-54)
     * W:月中的周数(1-5)
     * s:秒(0-59)
     * S:毫秒数(1-999)
     */
    @Test
    public void test9() throws ParseException {
        String patternStr = "G GG GGGGG E EE EEEEE a aa aaaaa";
        Date currDate = new Date();

        System.out.println("↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓中文地区模式↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓");
        System.out.println("====================Date->String====================");
        // 用SimpleDateFormat去格式化成非本地区域（默认Locale）的话，那就必须在构造的时候就指定好，如Locale.US
        DateFormat dateFormat = new SimpleDateFormat(patternStr, Locale.CHINA);
        System.out.println(dateFormat.format(currDate));

        System.out.println("====================String->Date====================");
        String dateStrParam = "公元 公元 公元 星期六 星期六 星期六 下午 下午 下午";
        System.out.println(dateFormat.parse(dateStrParam));

        System.out.println("↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓英文地区模式↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓");
        System.out.println("====================Date->String====================");
        dateFormat = new SimpleDateFormat(patternStr, Locale.US);
        System.out.println(dateFormat.format(currDate));

        System.out.println("====================String->Date====================");
        dateStrParam = "AD ad bC Sat SatUrday sunDay PM PM Am";
        System.out.println(dateFormat.parse(dateStrParam));
    }

    /**
     * java.util和java.sql包中竟然都有Date类，而且呢对它进行格式化/解析类竟然又跑到java.text去了，精神分裂啊
     * java.util.Date等类在建模日期的设计上行为不一致，缺陷明显。包括易变性、糟糕的偏移值、默认值、命名等等
     * java.util.Date同时包含日期和时间，而其子类java.sql.Date却仅包含日期，这是什么神继承？
     */
    @Test
    public void test10() {
        long currMillis = System.currentTimeMillis();

        Date date = new Date(currMillis);
        java.sql.Date sqlDate = new java.sql.Date(currMillis);
        Time time = new Time(currMillis);
        Timestamp timestamp = new Timestamp(currMillis);

        System.out.println("java.util.Date：" + date);
        System.out.println("java.sql.Date：" + sqlDate);
        System.out.println("java.sql.Time：" + time);
        System.out.println("java.sql.Timestamp：" + timestamp);
    }

    /**
     * 获取系统默认的ZoneId
     */
    @Test
    public void test11() {
        // JDK 1.8之前做法
        System.out.println(TimeZone.getDefault());
        // JDK 1.8之后做法
        System.out.println(ZoneId.systemDefault());
    }

    /**
     * 指定字符串得到一个ZoneId
     */
    @Test
    public void test12() {
        System.out.println(ZoneId.of("Asia/Shanghai"));
        // 报错：java.time.zone.ZoneRulesException: Unknown time-zone ID: Asia/xxx
        System.out.println(ZoneId.of("Asia/xxx"));
    }

    /**
     * 根据偏移量得到一个ZoneId
     * 第一个参数传的前缀，可用值为："GMT", "UTC", or "UT"。当然还可以传空串，那就直接返回第二个参数ZoneOffset。若以上都不是就报错
     */
    @Test
    public void test13() {
        ZoneId zoneId = ZoneId.ofOffset("UTC", ZoneOffset.of("+8"));
        System.out.println(zoneId);
        // 必须是大写的Z
        zoneId = ZoneId.ofOffset("UTC", ZoneOffset.of("Z"));
        System.out.println(zoneId);
    }

    /**
     * 从日期里面获得时区
     */
    @Test
    public void test14() {
        System.out.println(ZoneId.from(ZonedDateTime.now()));
        System.out.println(ZoneId.from(ZoneOffset.of("+8")));

        // 报错：java.time.DateTimeException: Unable to obtain ZoneId from TemporalAccessor:
        System.out.println(ZoneId.from(LocalDateTime.now())); // 虽然方法入参是TemporalAccessor，但是只接受带时区的类型，LocalXXX是不行的，使用时稍加注意。
        System.out.println(ZoneId.from(LocalDate.now()));
    }

    /**
     * 最小/最大偏移量：因为偏移量传入的是数字，这个是有限制的哦
     */
    @Test
    public void test15() {
        System.out.println("最小偏移量：" + ZoneOffset.MIN);
        System.out.println("最小偏移量：" + ZoneOffset.MAX);
        System.out.println("中心偏移量：" + ZoneOffset.UTC);
        // 超出最大范围
        System.out.println(ZoneOffset.of("+20"));
    }

    /**
     * 通过时分秒构造偏移量（使用很方便，推荐）
     */
    @Test
    public void test16() {
        System.out.println(ZoneOffset.ofHours(8));
        System.out.println(ZoneOffset.ofHoursMinutes(8, 8));
        System.out.println(ZoneOffset.ofHoursMinutesSeconds(8, 8, 8));

        System.out.println(ZoneOffset.ofHours(-5));

        // 指定一个精确的秒数  获取实例（有时候也很有用处）
        System.out.println(ZoneOffset.ofTotalSeconds(8 * 60 * 60));
    }

    /**
     * 对日期、时间进行了分开表示（LocalDate、LocalTime、LocalDateTime），对本地时间和带时区的时间进行了分开管理。
     * LocalXXX表示本地时间，也就是说是当前JVM所在时区的时间；
     * ZonedXXX表示是一个带有时区的日期时间，它们能非常方便的互相完成转换。
     */
    @Test
    public void test17() {
        // 本地日期/时间
        System.out.println("================本地时间================");
        System.out.println(LocalDate.now());
        System.out.println(LocalTime.now());
        System.out.println(LocalDateTime.now());

        // 时区时间
        System.out.println("================带时区的时间ZonedDateTime================");
        System.out.println(ZonedDateTime.now()); // 使用系统时区
        System.out.println(ZonedDateTime.now(ZoneId.of("America/New_York"))); // 自己指定时区
        System.out.println(ZonedDateTime.now(Clock.systemUTC())); // 自己指定时区
        System.out.println("================带时区的时间OffsetDateTime================");
        System.out.println(OffsetDateTime.now()); // 使用系统时区
        System.out.println(OffsetDateTime.now(ZoneId.of("America/New_York"))); // 自己指定时区
        System.out.println(OffsetDateTime.now(Clock.systemUTC())); // 自己指定时区
    }

    /**
     * 不带时区/偏移量的字符串
     */
    @Test
    public void test18() {
        String dateTimeStrParam = "2021-05-05T18:00";
        LocalDateTime localDateTime = LocalDateTime.parse(dateTimeStrParam);
        System.out.println("解析后：" + localDateTime);
    }

    /**
     * 带时区字/偏移量的符串
     */
    @Test
    public void test19() {
        // 带偏移量 使用OffsetDateTime
        String dateTimeStrParam = "2021-05-05T18:00-04:00";
        OffsetDateTime offsetDateTime = OffsetDateTime.parse(dateTimeStrParam);
        System.out.println("带偏移量解析后：" + offsetDateTime);

        // 带时区 使用ZonedDateTime
        dateTimeStrParam = "2021-05-05T18:00-05:00[America/New_York]";
        ZonedDateTime zonedDateTime = ZonedDateTime.parse(dateTimeStrParam);
        // 请注意带时区解析后这个结果：字符串参数偏移量明明是-05，为毛转换为ZonedDateTime后偏移量成为了-04呢？？？
        // 在2021.03.14 - 2021.11.07期间，纽约的偏移量是-4，其余时候是-5。
        // 本例的日期是2021-05-05处在夏令时之中，因此偏移量是-4，这就解释了为何你显示的写了-5最终还是成了-4。
        System.out.println("带时区解析后：" + zonedDateTime);
    }

    /**
     * DateTimeFormatter也是一个不可变的类，所以是线程安全的，比SimpleDateFormat靠谱多了
     * 另外它还内置了非常多的格式化模版实例供以使用
     */
    @Test
    public void test20() {
        System.out.println(DateTimeFormatter.ISO_LOCAL_DATE.format(LocalDate.now()));
        System.out.println(DateTimeFormatter.ISO_LOCAL_TIME.format(LocalTime.now()));
        System.out.println(DateTimeFormatter.ISO_LOCAL_DATE_TIME.format(LocalDateTime.now()));
    }

    /**
     * 若想自定义模式pattern，和Date一样它也可以自己指定任意的pattern 日期/时间模式
     */
    @Test
    public void test21() {
        // Q/q：季度(quarter)，如3; 03; Q3; 3rd quarter
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("第Q季度 yyyy-MM-dd HH:mm:ss", Locale.US);

        // 格式化输出
        System.out.println(formatter.format(LocalDateTime.now()));

        // 解析
        String dateTimeStrParam = "第1季度 2021-01-17 22:51:32";
        LocalDateTime localDateTime = LocalDateTime.parse(dateTimeStrParam, formatter);
        System.out.println("解析后的结果：" + localDateTime);
    }

    /**
     * LocalDateTime
     */
    @Test
    public void test_LocalDateTime() {
        LocalDateTime min = LocalDateTime.MIN;
        LocalDateTime max = LocalDateTime.MAX;

        System.out.println("LocalDateTime最小值：" + min);
        System.out.println("LocalDateTime最大值：" + max);
        System.out.println(min.getYear() + "-" + min.getMonthValue() + "-" + min.getDayOfMonth());
        System.out.println(max.getYear() + "-" + max.getMonthValue() + "-" + max.getDayOfMonth());

        System.out.println("当前时区的本地时间：" + LocalDateTime.now());
        System.out.println("当前时区的本地时间：" + LocalDateTime.of(LocalDate.now(), LocalTime.now()));
        System.out.println("纽约时区的本地时间：" + LocalDateTime.now(ZoneId.of("America/New_York")));


        LocalDateTime now = LocalDateTime.now(ZoneId.systemDefault());
        System.out.println("计算前：" + now);

        LocalDateTime after = now.plusDays(3);  // 加3天
        after = after.plusHours(-3); // 效果同now.minusDays(3);
        System.out.println("计算后：" + after);

        // 计算时间差
        Period period = Period.between(now.toLocalDate(), after.toLocalDate());
        System.out.println("相差天数：" + period.getDays());
        Duration duration = Duration.between(now.toLocalTime(), after.toLocalTime());
        System.out.println("相差小时数：" + duration.toHours());

        // System.out.println("格式化输出：" + DateTimeFormatter.ISO_LOCAL_DATE_TIME.format(now));
        System.out.println("格式化输出（本地化输出，中文环境）：" + DateTimeFormatter.ofLocalizedDateTime(FormatStyle.SHORT, FormatStyle.SHORT).format(now));

        String dateTimeStrParam = "2021-01-17 18:00:00";
        System.out.println("解析后输出：" + LocalDateTime.parse(dateTimeStrParam, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss", Locale.US)));
    }

    /**
     * OffsetDateTime是一个带有偏移量的日期时间类型。
     * 存储有精确到纳秒的日期时间，以及偏移量。
     * 可以简单理解为 OffsetDateTime = LocalDateTime + ZoneOffset。
     */
    @Test
    public void test_OffsetDateTime(){
        OffsetDateTime min = OffsetDateTime.MIN;
        OffsetDateTime max = OffsetDateTime.MAX;  // 偏移量的最大值是+18，最小值是-18，这是由ZoneOffset内部的限制决定的。

        System.out.println("OffsetDateTime最小值：" + min);
        System.out.println("OffsetDateTime最大值：" + max);
        System.out.println(min.getOffset() + ":" + min.getYear() + "-" + min.getMonthValue() + "-" + min.getDayOfMonth());
        System.out.println(max.getOffset() + ":" + max.getYear() + "-" + max.getMonthValue() + "-" + max.getDayOfMonth());

        System.out.println("当前位置偏移量的本地时间：" + OffsetDateTime.now());
        System.out.println("偏移量-4（纽约）的本地时间：：" + OffsetDateTime.of(LocalDateTime.now(), ZoneOffset.of("-4")));
        System.out.println("纽约时区的本地时间：" + OffsetDateTime.now(ZoneId.of("America/New_York")));

        OffsetDateTime now = OffsetDateTime.now(ZoneId.systemDefault());
        System.out.println("格式化输出（本地化输出，中文环境）：" + DateTimeFormatter.ofLocalizedDateTime(FormatStyle.SHORT, FormatStyle.SHORT).format(now));

        String dateTimeStrParam = "2021-01-17T18:00:00+07:00";
        System.out.println("解析后输出：" + OffsetDateTime.parse(dateTimeStrParam));

        LocalDateTime localDateTime = LocalDateTime.of(2021, 01, 17, 18, 00, 00);
        System.out.println("当前时区（北京）时间为：" + localDateTime);

        // 转换为偏移量为 -4的OffsetDateTime时间
        // 1、-4地方的晚上18点
        System.out.println("-4偏移量地方的晚上18点：" + OffsetDateTime.of(localDateTime, ZoneOffset.ofHours(-4)));
        System.out.println("-4偏移量地方的晚上18点（方式二）：" + localDateTime.atOffset(ZoneOffset.ofHours(-4)));
        // 2、北京时间晚上18:00 对应的-4地方的时间点
        System.out.println("当前地区对应的-4地方的时间：" + OffsetDateTime.ofInstant(localDateTime.toInstant(ZoneOffset.ofHours(8)), ZoneOffset.ofHours(-4)));

        OffsetDateTime offsetDateTime = OffsetDateTime.of(LocalDateTime.now(), ZoneOffset.ofHours(-4));
        System.out.println("-4偏移量时间为：" + offsetDateTime);

        // 转为LocalDateTime 注意：时间还是未变的哦
        System.out.println("LocalDateTime的表示形式：" + offsetDateTime.toLocalDateTime());
    }

    /**
     * ZonedDateTime
     */
    @Test
    public void test_ZonedDateTime() {
        System.out.println("当前位置偏移量的本地时间：" + ZonedDateTime.now());
        System.out.println("纽约时区的本地时间：" + ZonedDateTime.of(LocalDateTime.now(), ZoneId.of("America/New_York")));

        System.out.println("北京实现对应的纽约时区的本地时间：" + ZonedDateTime.now(ZoneId.of("America/New_York")));

        LocalDateTime localDateTime = LocalDateTime.of(2021, 01, 17, 18, 00, 00);
        System.out.println("当前时区（北京）时间为：" + localDateTime);

        // 转换为偏移量为 -4的OffsetDateTime时间
        // 1、-4地方的晚上18点
        System.out.println("纽约时区晚上18点：" + ZonedDateTime.of(localDateTime, ZoneId.of("America/New_York")));
        System.out.println("纽约时区晚上18点（方式二）：" + localDateTime.atZone(ZoneId.of("America/New_York")));
        // 2、北京时间晚上18:00 对应的-4地方的时间点
        System.out.println("北京地区此时间对应的纽约的时间：" + ZonedDateTime.ofInstant(localDateTime.toInstant(ZoneOffset.ofHours(8)), ZoneOffset.ofHours(-4)));
        System.out.println("北京地区此时间对应的纽约的时间：" + ZonedDateTime.ofInstant(localDateTime, ZoneOffset.ofHours(8), ZoneOffset.ofHours(-4)));

        OffsetDateTime offsetDateTime = OffsetDateTime.of(LocalDateTime.now(), ZoneOffset.ofHours(-4));
        System.out.println("-4偏移量时间为：" + offsetDateTime);

        // 转换为ZonedDateTime的表示形式
        System.out.println("ZonedDateTime的表示形式：" + offsetDateTime.toZonedDateTime());
        System.out.println("ZonedDateTime的表示形式：" + offsetDateTime.atZoneSameInstant(ZoneId.of("America/New_York")));
        System.out.println("ZonedDateTime的表示形式：" + offsetDateTime.atZoneSimilarLocal(ZoneId.of("America/New_York")));

        offsetDateTime =
                OffsetDateTime.of(LocalDateTime.of(2021, 05, 05, 18, 00, 00), ZoneOffset.ofHours(-4));
        System.out.println("-4偏移量时间为：" + offsetDateTime);

        // 转换为ZonedDateTime的表示形式
        System.out.println("ZonedDateTime的表示形式：" + offsetDateTime.toZonedDateTime());
        System.out.println("ZonedDateTime的表示形式：" + offsetDateTime.atZoneSameInstant(ZoneId.of("America/New_York")));
        System.out.println("ZonedDateTime的表示形式：" + offsetDateTime.atZoneSimilarLocal(ZoneId.of("America/New_York")));
    }

}
