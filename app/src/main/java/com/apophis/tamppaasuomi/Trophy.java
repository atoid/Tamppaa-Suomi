package com.apophis.tamppaasuomi;

import android.content.res.Resources;
import java.util.ArrayList;
import java.util.Locale;

public class Trophy {
    private static final float COUNT_COMPLETE = 95091.f;

    static class TrophyItem {
        public int name_id;
        public String name;
        public TrophyType type;
        public int count;
        public int x, y;
        public int level;

        public TrophyItem(int name_id, int count) {
            this.name_id = name_id;
            this.type = TrophyType.COUNTER;
            this.count = count;
            this.level = R.string.tr_x;
        }

        public TrophyItem(int name_id, int count, int level) {
            this.name_id = name_id;
            this.type = TrophyType.COUNTER;
            this.count = count;
            this.level = level;
        }

        public TrophyItem(String name, int x, int y) {
            this.name = name;
            this.type = TrophyType.LOCATION;
            this.x = x;
            this.y = y;
            this.level = R.string.tr_x;
        }

        public TrophyItem(String name, int x, int y, int level) {
            this.name = name;
            this.type = TrophyType.LOCATION;
            this.x = x;
            this.y = y;
            this.level = level;
        }
    }

    enum TrophyType {
        COUNTER,
        LOCATION
    }

    private static final TrophyItem[] counters = {
            new TrophyItem(R.string.tr_1, 1),
            new TrophyItem(R.string.tr_5, 5),
            new TrophyItem(R.string.tr_10, 10),
            new TrophyItem(R.string.tr_50, 50),
            new TrophyItem(R.string.tr_100, 100),
            new TrophyItem(R.string.tr_500, 500),
            new TrophyItem(R.string.tr_1000, 1000),
            new TrophyItem(R.string.tr_2000, 2000),
            new TrophyItem(R.string.tr_5000, 5000),
            new TrophyItem(R.string.tr_10000, 10000, R.string.tr_xx),
            new TrophyItem(R.string.tr_20000, 20000, R.string.tr_xx),
            new TrophyItem(R.string.tr_30000, 30000, R.string.tr_xx),
            new TrophyItem(R.string.tr_40000, 40000, R.string.tr_xx),
            new TrophyItem(R.string.tr_47545, 47545, R.string.tr_xx),
            new TrophyItem(R.string.tr_50000, 50000, R.string.tr_xx),
            new TrophyItem(R.string.tr_60000, 60000, R.string.tr_xx),
            new TrophyItem(R.string.tr_70000, 70000, R.string.tr_xx),
            new TrophyItem(R.string.tr_80000, 80000, R.string.tr_xx),
            new TrophyItem(R.string.tr_90000, 90000, R.string.tr_xx),
            new TrophyItem(R.string.tr_95091, 95091, R.string.tr_xxx)
    };

    private static final TrophyItem[] locations = {
            new TrophyItem("Angeli", 208, 85),
            new TrophyItem("Poistha", 212, 83),
            new TrophyItem("Ärjä", 242, 337),
            new TrophyItem("OL3", 88, 496),
            new TrophyItem("Lake Bodom", 171, 555),
            new TrophyItem("Annumamma", 101, 487),
            new TrophyItem("Ruotsi", 67, 235),
            new TrophyItem("Haaparanta", 170, 251),
            new TrophyItem("Oulu", 199, 302),
            new TrophyItem("Rauma", 99, 483),
            new TrophyItem("Pori", 89, 502),
            new TrophyItem("Kajjjaani", 251, 340, R.string.tr_xx),
            new TrophyItem("Sinettä", 201, 208),
            new TrophyItem("Joulupukki", 275, 129),
            new TrophyItem("Kaamanen",238,74),
            new TrophyItem("Pomokaira", 222, 138),
            new TrophyItem("Nuorgam", 250, 21),
            new TrophyItem("Ivalo", 245, 99),
            new TrophyItem("Kilpisjärvi",114,73),
            new TrophyItem("Raattama", 177,125),
            new TrophyItem("Tankavaara",236, 124),
            new TrophyItem("Levi", 190, 145),
            new TrophyItem("Ylläs", 176, 157),
            new TrophyItem("Sodankylä", 225, 166),
            new TrophyItem("Naruska", 280, 180),
            new TrophyItem("Kemijärvi", 243, 204),
            new TrophyItem("Kuusamo", 282, 244),
            new TrophyItem("Jääkarhut", 223, 247),
            new TrophyItem("Ii", 197, 280),
            new TrophyItem("Isosyöte", 245, 260),
            new TrophyItem("Hossa", 292, 272),
            new TrophyItem("Raatteentie", 288, 305),
            new TrophyItem("Lieksa", 308, 387),
            new TrophyItem("Lappajärvi", 152, 397),
            new TrophyItem("Laihia", 111, 403),
            new TrophyItem("Kuopio", 250, 413),
            new TrophyItem("Lappeen ranta", 265, 512),
            new TrophyItem("Atomimiilu", 213, 545),
            new TrophyItem("Hanko", 124, 576),
            new TrophyItem("Helsinki", 178, 560),
            new TrophyItem("Heinola", 209, 504),
            new TrophyItem("Nokia", 143, 487),
            new TrophyItem("Tampere", 151, 486),
            new TrophyItem("Forssa", 144, 523),
            new TrophyItem("Joensuu", 303, 426),
            new TrophyItem("Möhkö", 341, 423),
            new TrophyItem("Jyväskylä", 203, 447),
            new TrophyItem("Närpiö", 91, 429),
            new TrophyItem("Pandat", 161, 429),
            new TrophyItem("Turku", 107, 540),
            new TrophyItem("Ahvenanmaa", 42, 549),
            new TrophyItem("Myrskyluodon Maija", 56, 537),
            new TrophyItem("Norppa", 265, 495),
            new TrophyItem("Leijona", 282, 499),
            new TrophyItem("Wincapita", 216, 427),

            new TrophyItem("Öljynporauslautta", 31, 7, R.string.tr_xx),
            new TrophyItem("Norja", 60, 65, R.string.tr_xx),
            new TrophyItem("Ruotsi", 69, 234, R.string.tr_xx),
            new TrophyItem("Ankkuri", 49, 444, R.string.tr_xx),
            new TrophyItem("Estonia", 86, 597, R.string.tr_xx),
            new TrophyItem("Rahtilaiva", 134, 598, R.string.tr_xx),
            new TrophyItem("Sukhoi", 302, 565, R.string.tr_xx),
            new TrophyItem("Jolla", 336, 504, R.string.tr_xx),
            new TrophyItem("Viro", 183, 605, R.string.tr_xx),
            new TrophyItem("Neuvostoliitto", 316, 211, R.string.tr_xx),
            new TrophyItem("Sukellusvene", 317, 18, R.string.tr_xx),

    };

    public static String getProgress() {
        StringBuilder msg = new StringBuilder();
        float count = Util.getTapCount();
        float percent = (count / COUNT_COMPLETE) * 100.f;
        msg.append(Util.getTapCount());
        msg.append(" / ");
        if (percent < 100.f) {
            msg.append(String.format(Locale.ROOT, "%.1f", percent));
        }
        else {
            msg.append("100");
        }
        msg.append("%");
        return msg.toString();
    }

    public static TrophyItem[] checkSingle(int x, int y, int count)
    {
        int i;
        TrophyItem[] res = {null, null};

        for (i = 0; i < counters.length; i++) {
            TrophyItem ti = counters[i];
            if (ti.count == count) {
                res[0] = ti;
                break;
            }
        }

        for (i = 0; i < locations.length; i++) {
            TrophyItem ti = locations[i];
            if (ti.x == x && ti.y == y) {
                res[1] = ti;
                break;
            }
        }

        return res;
    }

    public static String getAsString(Resources r, TrophyItem trophyItem, boolean append) {
        String msg = r.getString(trophyItem.level);

        if (trophyItem.name != null) {
            msg += trophyItem.name;
        } else {
            msg += r.getString(trophyItem.name_id);
        }

        if (append) {
            msg += r.getString(R.string.tr_append);
        }

        return msg;
    }

    public static ArrayList<TrophyItem> getCompleted() {
        ArrayList<TrophyItem> arr = new ArrayList<>();
        int i;

        for (i = 0; i < counters.length; i++) {
            TrophyItem ti = counters[i];
            if (Util.getTapCount() >= ti.count) {
                arr.add(ti);
            }
        }

        for (i = 0; i < locations.length; i++) {
            TrophyItem ti = locations[i];
            int tapBit = Util.getTapBit(ti.x, ti.y);
            if (tapBit != 0) {
                arr.add(ti);
            }
        }

        return arr;
    }

}
