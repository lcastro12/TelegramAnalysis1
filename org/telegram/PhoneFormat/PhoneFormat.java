package org.telegram.PhoneFormat;

import android.support.v4.view.MotionEventCompat;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;
import org.telegram.messenger.BuildConfig;
import org.telegram.ui.ApplicationLoader;

public class PhoneFormat {
    public static PhoneFormat Instance = new PhoneFormat();
    public ByteBuffer buffer;
    public HashMap<String, ArrayList<String>> callingCodeCountries;
    public HashMap<String, CallingCodeInfo> callingCodeData;
    public HashMap<String, Integer> callingCodeOffsets;
    public HashMap<String, String> countryCallingCode;
    public byte[] data;
    public String defaultCallingCode;
    public String defaultCountry;
    private boolean initialzed = false;

    public static String strip(String str) {
        StringBuilder res = new StringBuilder(str);
        String phoneChars = "0123456789+*#";
        for (int i = res.length() - 1; i >= 0; i--) {
            if (!phoneChars.contains(res.substring(i, i + 1))) {
                res.deleteCharAt(i);
            }
        }
        return res.toString();
    }

    public static String stripExceptNumbers(String str) {
        StringBuilder res = new StringBuilder(str);
        String phoneChars = "0123456789";
        for (int i = res.length() - 1; i >= 0; i--) {
            if (!phoneChars.contains(res.substring(i, i + 1))) {
                res.deleteCharAt(i);
            }
        }
        return res.toString();
    }

    public PhoneFormat() {
        init(null);
    }

    public PhoneFormat(String countryCode) {
        init(countryCode);
    }

    public void init(String countryCode) {
        try {
            InputStream stream = ApplicationLoader.applicationContext.getAssets().open("PhoneFormats.dat");
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            byte[] buf = new byte[1024];
            while (true) {
                int len = stream.read(buf, 0, 1024);
                if (len == -1) {
                    break;
                }
                bos.write(buf, 0, len);
            }
            this.data = bos.toByteArray();
            this.buffer = ByteBuffer.wrap(this.data);
            this.buffer.order(ByteOrder.LITTLE_ENDIAN);
            if (countryCode == null || countryCode.length() == 0) {
                this.defaultCountry = Locale.getDefault().getCountry().toLowerCase();
            } else {
                this.defaultCountry = countryCode;
            }
            this.callingCodeOffsets = new HashMap(MotionEventCompat.ACTION_MASK);
            this.callingCodeCountries = new HashMap(MotionEventCompat.ACTION_MASK);
            this.callingCodeData = new HashMap(10);
            this.countryCallingCode = new HashMap(MotionEventCompat.ACTION_MASK);
            parseDataHeader();
            this.initialzed = true;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public String defaultCallingCode() {
        return callingCodeForCountryCode(this.defaultCountry);
    }

    public String callingCodeForCountryCode(String countryCode) {
        return (String) this.countryCallingCode.get(countryCode.toLowerCase());
    }

    public ArrayList countriesForCallingCode(String callingCode) {
        if (callingCode.startsWith("+")) {
            callingCode = callingCode.substring(1);
        }
        return (ArrayList) this.callingCodeCountries.get(callingCode);
    }

    public CallingCodeInfo findCallingCodeInfo(String str) {
        CallingCodeInfo res = null;
        int i = 0;
        while (i < 3 && i < str.length()) {
            res = callingCodeInfo(str.substring(0, i + 1));
            if (res != null) {
                break;
            }
            i++;
        }
        return res;
    }

    public String format(String orig) {
        if (!this.initialzed) {
            return orig;
        }
        String str = strip(orig);
        String rest;
        CallingCodeInfo info;
        if (str.startsWith("+")) {
            rest = str.substring(1);
            info = findCallingCodeInfo(rest);
            if (info == null) {
                return orig;
            }
            return "+" + info.format(rest);
        }
        info = callingCodeInfo(this.defaultCallingCode);
        if (info == null) {
            return orig;
        }
        String accessCode = info.matchingAccessCode(str);
        if (accessCode == null) {
            return info.format(str);
        }
        rest = str.substring(accessCode.length());
        String phone = rest;
        CallingCodeInfo info2 = findCallingCodeInfo(rest);
        if (info2 != null) {
            phone = info2.format(rest);
        }
        if (phone.length() == 0) {
            return accessCode;
        }
        return String.format("%s %s", new Object[]{accessCode, phone});
    }

    public boolean isPhoneNumberValid(String phoneNumber) {
        if (!this.initialzed) {
            return true;
        }
        String str = strip(phoneNumber);
        CallingCodeInfo info;
        if (str.startsWith("+")) {
            String rest = str.substring(1);
            info = findCallingCodeInfo(rest);
            if (info == null || !info.isValidPhoneNumber(rest)) {
                return false;
            }
            return true;
        }
        info = callingCodeInfo(this.defaultCallingCode);
        if (info == null) {
            return false;
        }
        String accessCode = info.matchingAccessCode(str);
        if (accessCode == null) {
            return info.isValidPhoneNumber(str);
        }
        rest = str.substring(accessCode.length());
        if (rest.length() == 0) {
            return false;
        }
        CallingCodeInfo info2 = findCallingCodeInfo(rest);
        if (info2 == null || !info2.isValidPhoneNumber(rest)) {
            return false;
        }
        return true;
    }

    int value32(int offset) {
        if (offset + 4 > this.data.length) {
            return 0;
        }
        this.buffer.position(offset);
        return this.buffer.getInt();
    }

    short value16(int offset) {
        if (offset + 2 > this.data.length) {
            return (short) 0;
        }
        this.buffer.position(offset);
        return this.buffer.getShort();
    }

    public String valueString(int offset) {
        int a = offset;
        while (a < this.data.length) {
            try {
                if (this.data[a] != (byte) 0) {
                    a++;
                } else if (offset == a - offset) {
                    return BuildConfig.FLAVOR;
                } else {
                    return new String(this.data, offset, a - offset);
                }
            } catch (Exception e) {
                e.printStackTrace();
                return BuildConfig.FLAVOR;
            }
        }
        return BuildConfig.FLAVOR;
    }

    public CallingCodeInfo callingCodeInfo(String callingCode) {
        CallingCodeInfo res = (CallingCodeInfo) this.callingCodeData.get(callingCode);
        if (res == null) {
            Integer num = (Integer) this.callingCodeOffsets.get(callingCode);
            if (num != null) {
                String str;
                byte[] bytes = this.data;
                int start = num.intValue();
                int offset = start;
                res = new CallingCodeInfo();
                res.callingCode = callingCode;
                res.countries = (ArrayList) this.callingCodeCountries.get(callingCode);
                this.callingCodeData.put(callingCode, res);
                int block1Len = value16(offset);
                offset = (offset + 2) + 2;
                int block2Len = value16(offset);
                offset = (offset + 2) + 2;
                int setCnt = value16(offset);
                offset = (offset + 2) + 2;
                ArrayList<String> strs = new ArrayList(5);
                while (true) {
                    str = valueString(offset);
                    if (str.length() == 0) {
                        break;
                    }
                    strs.add(str);
                    offset += str.length() + 1;
                }
                res.trunkPrefixes = strs;
                offset++;
                strs = new ArrayList(5);
                while (true) {
                    str = valueString(offset);
                    if (str.length() == 0) {
                        break;
                    }
                    strs.add(str);
                    offset += str.length() + 1;
                }
                res.intlPrefixes = strs;
                ArrayList<RuleSet> ruleSets = new ArrayList(setCnt);
                offset = start + block1Len;
                for (int s = 0; s < setCnt; s++) {
                    RuleSet ruleSet = new RuleSet();
                    ruleSet.matchLen = value16(offset);
                    offset += 2;
                    int ruleCnt = value16(offset);
                    offset += 2;
                    ArrayList<PhoneRule> arrayList = new ArrayList(ruleCnt);
                    for (int r = 0; r < ruleCnt; r++) {
                        PhoneRule rule = new PhoneRule();
                        rule.minVal = value32(offset);
                        offset += 4;
                        rule.maxVal = value32(offset);
                        offset += 4;
                        int offset2 = offset + 1;
                        rule.byte8 = bytes[offset];
                        offset = offset2 + 1;
                        rule.maxLen = bytes[offset2];
                        offset2 = offset + 1;
                        rule.otherFlag = bytes[offset];
                        offset = offset2 + 1;
                        rule.prefixLen = bytes[offset2];
                        offset2 = offset + 1;
                        rule.flag12 = bytes[offset];
                        offset = offset2 + 1;
                        rule.flag13 = bytes[offset2];
                        int strOffset = value16(offset);
                        offset += 2;
                        rule.format = valueString(((start + block1Len) + block2Len) + strOffset);
                        if (rule.format.indexOf("[[") != -1) {
                            int closePos = rule.format.indexOf("]]");
                            rule.format = String.format("%s%s", new Object[]{rule.format.substring(0, openPos), rule.format.substring(closePos + 2)});
                        }
                        arrayList.add(rule);
                        if (rule.hasIntlPrefix) {
                            ruleSet.hasRuleWithIntlPrefix = true;
                        }
                        if (rule.hasTrunkPrefix) {
                            ruleSet.hasRuleWithTrunkPrefix = true;
                        }
                    }
                    ruleSet.rules = arrayList;
                    ruleSets.add(ruleSet);
                }
                res.ruleSets = ruleSets;
            }
        }
        return res;
    }

    public void parseDataHeader() {
        int count = value32(0);
        int base = (count * 12) + 4;
        int spot = 4;
        for (int i = 0; i < count; i++) {
            String callingCode = valueString(spot);
            spot += 4;
            String country = valueString(spot);
            spot += 4;
            int offset = value32(spot) + base;
            spot += 4;
            if (country.equals(this.defaultCountry)) {
                this.defaultCallingCode = callingCode;
            }
            this.countryCallingCode.put(country, callingCode);
            this.callingCodeOffsets.put(callingCode, Integer.valueOf(offset));
            ArrayList<String> countries = (ArrayList) this.callingCodeCountries.get(callingCode);
            if (countries == null) {
                countries = new ArrayList();
                this.callingCodeCountries.put(callingCode, countries);
            }
            countries.add(country);
        }
        if (this.defaultCallingCode != null) {
            callingCodeInfo(this.defaultCallingCode);
        }
    }
}
