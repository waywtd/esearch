package com.globalegrow.esearch.hive.udf;

import com.globalegrow.esearch.util.StringUtils;
import org.apache.hadoop.hive.ql.exec.UDF;
import org.apache.hadoop.io.Text;

public class DcLangUDF extends UDF {

    public String evaluate(Text t1, Text t2) {
        String lang = "en";
        if(StringUtils.isNotEmpty(t1) && StringUtils.isNotEmpty(t2)){
            String dc = t1.toString();
            String ubcd = t2.toString();
            if ("10002".equals(ubcd)) {
                switch (dc) {
                    case "1301":
                        lang = "en";
                        break;
                    case "1302":
                        lang = "ru";
                        break;
                    case "1303":
                        lang = "es";
                        break;
                    case "1304":
                        lang = "en";
                        break;
                    case "1305":
                        lang = "en";
                        break;
                    case "1306":
                        lang = "it";
                        break;
                    case "1307":
                        lang = "de";
                        break;
                    case "1308":
                        lang = "pt";
                        break;
                    case "1309":
                        lang = "br";
                        break;
                    case "1310":
                        lang = "fr";
                        break;
                    case "1311":
                        lang = "pt-tr";
                        break;
                    case "1312":
                        lang = "en";
                        break;
                    case "1313":
                        lang = "pl";
                        break;
                    case "1314":
                        lang = "en";
                        break;
                    case "1315":
                        lang = "gr";
                        break;
                    case "1316":
                        lang = "ep-mx";
                        break;
                    case "1317":
                        lang = "hu";
                        break;
                    case "1318":
                        lang = "sk";
                        break;
                    case "1319":
                        lang = "cz";
                        break;
                    case "1320":
                        lang = "nl";
                        break;
                    case "1321":
                        lang = "ro";
                        break;
                    case "1322":
                        lang = "ma";
                        break;
                    case "1323":
                        lang = "jp";
                        break;
                    case "1324":
                        lang = "ua";
                        break;
                    case "1325":
                        lang = "bg";
                        break;
                    case "1326":
                        lang = "hr";
                        break;
                    case "1327":
                        lang = "se";
                        break;
                    case "1328":
                        lang = "fi";
                        break;
                    case "1329":
                        lang = "no";
                        break;
                    case "1330":
                        lang = "dk";
                        break;
                    case "1331":
                        lang = "th";
                        break;
                    case "1332":
                        lang = "vn";
                        break;
                    case "1333":
                        lang = "lk";
                        break;
                    case "1334":
                        lang = "db";
                        break;
                    case "1335":
                        lang = "id";
                        break;
                    case "1336":
                        lang = "kz";
                        break;
                    case "1337":
                        lang = "il";
                        break;
                    default:
                        lang = "o";
                        break;
                }
            } else if ("10013".equals(ubcd) || "10007".equals(ubcd)) {
                switch (dc) {
                    case "1301":
                        lang = "en";
                        break;
                    case "1302":
                        lang = "fr";
                        break;
                    case "1303":
                        lang = "es";
                        break;
                    case "1304":
                        lang = "pt";
                        break;
                    case "1305":
                        lang = "de";
                        break;
                    case "1306":
                        lang = "ar";
                        break;
                    case "1307":
                        lang = "it";
                        break;
                    case "1308":
                        lang = "ru";
                        break;
                    default:
                        lang = "o";
                        break;
                }
            }

        }
        return lang;
    }

}