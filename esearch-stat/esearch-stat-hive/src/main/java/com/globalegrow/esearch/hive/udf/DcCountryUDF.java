package com.globalegrow.esearch.hive.udf;

import com.globalegrow.esearch.util.StringUtils;
import org.apache.hadoop.hive.ql.exec.UDF;
import org.apache.hadoop.io.Text;

public class DcCountryUDF extends UDF
{

    public String evaluate(Text t1)
    {
        String country = "cn";
        if(StringUtils.isNotEmpty(t1)){
            String dc = t1.toString();
            switch (dc) {
                case "1301":
                    country = "en";
                    break;
                case "1302":
                    country = "ru";
                    break;
                case "1303":
                    country = "es";
                    break;
                case "1304":
                    country = "uk";
                    break;
                case "1305":
                    country = "us";
                    break;
                case "1306":
                    country = "it";
                    break;
                case "1307":
                    country = "de";
                    break;
                case "1308":
                    country = "pt";
                    break;
                case "1309":
                    country = "br";
                    break;
                case "1310":
                    country = "fr";
                    break;
                case "1311":
                    country = "tr";
                    break;
                case "1312":
                    country = "au";
                    break;
                case "1313":
                    country = "pl";
                    break;
                case "1314":
                    country = "in";
                    break;
                case "1315":
                    country = "gr";
                    break;
                case "1316":
                    country = "mx";
                    break;
                case "1317":
                    country = "hu";
                    break;
                case "1318":
                    country = "sk";
                    break;
                case "1319":
                    country = "cz";
                    break;
                case "1320":
                    country = "nl";
                    break;
                case "1321":
                    country = "ro";
                    break;
                case "1322":
                    country = "ma";
                    break;
                case "1323":
                    country = "jp";
                    break;
                case "1324":
                    country = "ua";
                    break;
                case "1325":
                    country = "bg";
                    break;
                case "1326":
                    country = "hr";
                    break;
                case "1327":
                    country = "se";
                    break;
                case "1328":
                    country = "fi";
                    break;
                case "1329":
                    country = "no";
                    break;
                case "1330":
                    country = "dk";
                    break;
                case "1331":
                    country = "th";
                    break;
                case "1332":
                    country = "vn";
                    break;
                case "1333":
                    country = "lk";
                    break;
                case "1334":
                    country = "db";
                    break;
                case "1335":
                    country = "id";
                    break;
                case "1336":
                    country = "kz";
                    break;
                case "1337":
                    country = "il";
                    break;
                default:
                    country = "o";
                    break;
            }
        }
        return country;
    }

}