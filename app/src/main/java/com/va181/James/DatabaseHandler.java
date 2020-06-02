package com.va181.James;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class DatabaseHandler extends SQLiteOpenHelper {

    private final static int DATABASE_VERSION = 2;
    private final static String DATABASE_NAME = "db_film";
    private final static String TABLE_FILM = "t_film";
    private final static String KEY_ID_FILM = "ID_Film";
    private final static String KEY_JUDUL = "Judul";
    private final static String KEY_TGL = "Tanggal";
    private final static String KEY_GAMBAR = "Gambar";
    private final static String KEY_AKTOR= "Aktor";
    private final static String KEY_GENRE = "Genre";
    private final static String KEY_SINOPSIS = "Sinopsis";
    private final static String KEY_LINK = "Link";
    private SimpleDateFormat sdFromat = new SimpleDateFormat("dd/MM/yyyy hh:mm", Locale.getDefault());
    private Context context;

    public DatabaseHandler(Context ctx) {
        super(ctx, DATABASE_NAME, null, DATABASE_VERSION);
        this.context = ctx;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_TABLE_BERITA = "CREATE TABLE " + TABLE_FILM
                + "(" + KEY_ID_FILM + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + KEY_JUDUL + " TEXT, " + KEY_TGL + " DATE, "
                + KEY_GAMBAR + " TEXT, " + KEY_AKTOR + " TEXT, "
                + KEY_GENRE+ " TEXT, " + KEY_SINOPSIS + " TEXT, "
                + KEY_LINK + " TEXT);";

        db.execSQL(CREATE_TABLE_BERITA);
        inisialisasiFilmAwal(db);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        String DROP_TABLE = "DROP TABLE IF EXISTS " + TABLE_FILM;
        db.execSQL(DROP_TABLE);
        onCreate(db);

    }

    public void tambahFilm(Film dataFilm) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues cv = new ContentValues();

        cv.put(KEY_JUDUL, dataFilm.getJudul());
        cv.put(KEY_TGL, sdFromat.format(dataFilm.getTanggal()));
        cv.put(KEY_GAMBAR, dataFilm.getGambar());
        cv.put(KEY_AKTOR, dataFilm.getAktor ());
        cv.put(KEY_GENRE, dataFilm.getGenre ());
        cv.put(KEY_SINOPSIS, dataFilm.getSinopsis ());
        cv.put(KEY_LINK, dataFilm.getLink());

        db.insert(TABLE_FILM, null, cv);
        db.close();
    }

    public void tambahFilm(Film dataFilm, SQLiteDatabase db) {
        ContentValues cv = new ContentValues();

        cv.put(KEY_JUDUL, dataFilm.getJudul());
        cv.put(KEY_TGL, sdFromat.format(dataFilm.getTanggal()));
        cv.put(KEY_GAMBAR, dataFilm.getGambar());
        cv.put(KEY_AKTOR, dataFilm.getAktor ());
        cv.put(KEY_GENRE, dataFilm.getGenre ());
        cv.put(KEY_SINOPSIS, dataFilm.getSinopsis ());
        cv.put(KEY_LINK, dataFilm.getLink());
        db.insert(TABLE_FILM, null, cv);
    }

    public void editFilm(Film dataFilm) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues cv = new ContentValues();

        cv.put(KEY_JUDUL,  dataFilm.getJudul());
        cv.put(KEY_TGL, sdFromat.format( dataFilm.getTanggal()));
        cv.put(KEY_GAMBAR, dataFilm.getGambar());
        cv.put(KEY_AKTOR,  dataFilm.getAktor ());
        cv.put(KEY_GENRE,  dataFilm.getGenre ());
        cv.put(KEY_SINOPSIS,  dataFilm.getSinopsis ());
        cv.put(KEY_LINK,  dataFilm.getLink());

        db.update(TABLE_FILM, cv, KEY_ID_FILM + "=?", new String[]{String.valueOf(dataFilm.getIdFilm ())});

        db.close();
    }

    public void  hapusFilm(int idFilm) {
        SQLiteDatabase db = getWritableDatabase();
        db.delete(TABLE_FILM, KEY_ID_FILM + "=?", new String[]{String.valueOf(idFilm)});
        db.close();
    }

    public ArrayList<Film> getAllFilm() {
        ArrayList<Film> dataFilm = new ArrayList<>();
        String query = "SELECT * FROM " + TABLE_FILM;
        SQLiteDatabase db = getReadableDatabase();
        Cursor csr = db.rawQuery(query, null);
        if (csr.moveToFirst()){
            do{
                Date tempDate = new Date();
                try {
                    tempDate = sdFromat.parse(csr.getString(2));
                } catch (ParseException er) {
                    er.printStackTrace();
                }

                Film tempFilm = new Film (
                        csr.getInt(0),
                        csr.getString(1),
                        tempDate,
                        csr.getString(3),
                        csr.getString(4),
                        csr.getString(5),
                        csr.getString(6),
                        csr.getString(7)
                );

                dataFilm.add(tempFilm);
            } while (csr.moveToNext());
        }

        return dataFilm;

    }

    private String storeImageFile(int id) {
        String location;
        Bitmap image = BitmapFactory.decodeResource(context.getResources(), id);
        location = InputActivity.saveImageToInternalStorage(image, context);
        return  location;
    }

    private void inisialisasiFilmAwal(SQLiteDatabase db) {
        int idFilm = 0;
        Date tempDate = new Date();

        //Menambah data film ke-1
        try {
            tempDate = sdFromat.parse("8 Mei 2014 ");
        } catch (ParseException er) {
            er.printStackTrace();
        }



        Film film1 = new Film (
                idFilm,
                "Marmut Merah Jambu",
                tempDate,
                storeImageFile(R.drawable.foto1),
                  "Christoffer Nelwan\n" +
                          "Efranda Stephanus\n" +
                          "Raditya Dika\n" +
                          "Sonya Pandarmawan\n" +
                          "Kamga Mo\n" +
                          "Anjani Dina",
                "Drama Komedi",
                "Marmut Merah Jambu ini berkisah tentang Dika yang menceritakan kisah cinta pertamanya ketika masa SMA, dengan perempuan bernama Ina Mangunkusumo.[3] Selain itu dikisahkan pula saat Dika dan temannya Bertus yang membentuk grup detektif untuk memecahkan masalah teman-temannya, juga persahabatannya dengan Cindy.\n" +
                        "\n" +
                        "Suatu hari Dika bertemu dengan bapaknya Ina Mangunkusumo, cinta pertamanya di SMA. Dika menceritakan usahanya membuat grup detektif untuk menarik perhatian Ina. Grup itu dibuat bersama Bertus, temannya yang sama-sama anak terbuang di sekolah. Dika juga bercerita tentang persahabatannya dengan cewek unik bernama Cindy di SMA. Seiring dengan cerita Dika, dia sadar: ada kasus pada masa lalunya yang belum selesai hingga dia dewasa. Seiring dia berusaha memecahkannya, seiring itu pula dia bertanya, benarkah cinta pertama enggak kemana-mana?",
                "https://www.youtube.com/watch?v=4YlR7Vu2Py0"
        );

        tambahFilm(film1, db);
        idFilm++;

        // Data film ke-2
        try {
            tempDate = sdFromat.parse("25/01/2018 ");
        } catch (ParseException er) {
            er.printStackTrace();
        }
        Film film2 = new Film (
                idFilm,
                "Jailangkung 2",
                tempDate,
                storeImageFile(R.drawable.foto2),
                "\tAmanda Rawles\n" +
                        "Jefri Nichol\n" +
                        "Hannah Al Rashid\n" +
                        "Lukman Sardi",
                "Horror",
                "Kisah berawal kala Tasya (Gabriella), adik Bella, merasa kesepian sendirian di rumah. Tasya yang tak pernah mendapat kasih sayang seorang ibu, ingin sekali bisa melihat sosok yang telah melahirkannya itu. Dia ingat ketika diam-diam melihat ayahnya (Lukman Sardi), saat memainkan boneka jailangkung dan memanggil arwah mendiang istrinya. Tasya lalu membuat sendiri boneka jailangkung dan melakukan ritual, berharap ibunya datang.Sayangnya apa yang dilakukan Tasya membawa malapetaka. Bukannya bertemu ibu, Tasya malah dibawa ke dunia lain oleh makhluk gaib. Tak cuma sampai di situ, Angel (Hannah), kakak dari Tasya dan Bella, juga sering diganggu oleh makhluk-makhluk dari boneka jailangkung. Tubuh Angel dirasuki oleh titisan setan.\n" +
                        "\n" +
                        "Dari kejadian-kejadian yang dialami dua saudaranya inilah, Bella tentu tak tinggal diam. Dia lalu meminta bantuan teman sekampusnya, Rama (Jefri), yang memang suka dengan hal-hal berbau mistis. Bukan cuma itu, mereka berdua bertemu dan dibantu juga oleh sosok Bram (Naufal Samudra). Bella ditemani kedua rekannya pun rela menuju ke tempat-tempat berbahaya demi menyelamatkan keluarganya, bahkan sampai ke dasar laut.",
                "https://www.youtube.com/watch?v=nTRbdzWkW34&feature=emb_title"
        );

        tambahFilm(film2, db);
        idFilm++;

        //Data film ke-3
        try {
            tempDate = sdFromat.parse("15 Juni 2018 ");
        } catch (ParseException er) {
            er.printStackTrace();
        }
        Film film3 = new Film (
                idFilm,
                "Kuntilanak 3",
                tempDate,
                storeImageFile(R.drawable.foto3),
                "Julie Estelle\n" +
                        "Imelda Therinne\n" +
                        "Laura Antoinette\n" +
                        "Mandala Abadi Shoji\n" +
                        "Reza Pahlevi\n" +
                        "Ida Iasha\n" +
                        "Irene Racik Salamun\n" +
                        "Cindy Valerie\n" +
                        "Robby Kolbe\n" +
                        "Laudya Cynthia Bella",
                "Horror",
                "Diawali dengan menghilangnya pasangan yang baru saja bertunangan, yaitu Stella (Laudya Chintya Bella) dan tunangannya Rimson (Robby Kolbe) di sebuah hutan, keempat anggota tim SAR Komodo, Darwin (Mandala Shoji), Asti (Imelda Therinne), Herman (Reza Pahlevi) dan Petra (Laura Antoinetta) kini bersama dengan tim SAR udara, Albatros untuk mencari kedua orang tersebut. Tim SAR Komodo yang menelusuri lewat jalur darat, menemukan Samantha (Julie Estelle) dan membujuknya untuk bersama mereka melakukan perjalanan. Sesampai di hutan dan terblokirnya jalan tanah, mereka terpaksa mendaki bukit berselimut hutan belantara.\n" +
                        "\n" +
                        "Ketika mereka sudah mendirikan kemah, Samantha didesak oleh kru tim SAR Komodo agar mengutarakan alasan perginya ia ke hutan itu. Samantha, justru menyarankan agar mereka berhenti mencari kedua teman mereka agar tidak terjadi sesuatu yang tidak diinginkan. Saat itu, mereka mendengar suara aneh dan fenomena gaib yang membuat mereka menjelajah sebagian kecil hutan, dalam kejadian itu, Samantha berhasil menemukan syal merah milik Stella. Mimpi Samantha menjadi penjelas, arwah Ibunya, Mega N. Widjoko (Ida Iasha) meminta Samantha untuk pergi ke Ujung Sedo yang berada di ujuung hutan belantara untuk mencabut wangsitnya atas Kuntilanak.",
                "https://www.youtube.com/watch?v=o2wTFJxXA-Q"
        );

        tambahFilm(film3, db);
        idFilm++;

        // Data film ke-4
        try {
            tempDate = sdFromat.parse("04/06/2019 ");
        } catch (ParseException er) {
            er.printStackTrace();
        }
        Film film4 = new Film (
                idFilm,
                "Pulau Hantu",
                tempDate,
                storeImageFile(R.drawable.foto4),
                "\tRicky Harun\n" +
                        "Kartika Ayuningtyas\n" +
                        "Yedihel Luntungan\n" +
                        "Indri Yanuarti\n" +
                        "Ufara Dzikri\n" +
                        "Abdurrahman Arif\n" +
                        "Bramasto\n" +
                        "Rini Oktaviani\n" +
                        "Ajeng Kartika",
                "Horror",
                "Dante, Nero, Abel, Mario, Meryl, Radella, Alisha, dan Tracy adalah delapan sahabat yang berencana pesiar ke Bunaken dengan menggunakan Kapal Pesiar Mini milik ayah Abel. Dalam perjalanan di laut, ayah Abel menelepon bahwa tindakan yang ternyata dilakukan Abel tanpa seizin sang ayah itu akan berbuah bencana karena kapal itu akan digunakan untuk menjemput relasi bisnis ayah Abel. Maka dari itu, daripada terpaksa berbelok arah, mereka menemukan sebuah pulau bernama Pulau Madara yang beresor tunggal. Kapal akan kembali ke Pulau Madara beberapa hari lagi.\n" +
                        "\n" +
                        "Ternyata disana suasananya menyenangkan. Dengan kekayaan Abel, mudah bagi mereka untuk “menguasai” pulau itu. Apalagi, pulau itu tidak ditempati oleh siapapun kecuali pengelola resor, Pak Tandu, dan kawan pembersih resornya, Lasar. Sedari awal, Pak Tandu memperingatkan kepada mereka agar tidak pergi ke belakang pulau baik melewati jalur darat ataupun laut. Hari pertama berjalan menyenangkan hingga saat makan malam, Lasar menceritakan Tania, seorang anak yang fotonya terpampang di meja makan. Tania adalah anak pemilik resort itu yang meninggal karena tenggelam di belakang pulau, naas mayatnya belum ditemukan. Hal itu juga membuat ayahnya shock dan sempat mencoba bunuh diri.",
                "https://www.youtube.com/watch?v=auUUza_C9Bc"
        );

        tambahFilm(film4, db);
        idFilm++;

        //Data film ke-5
        try {
            tempDate = sdFromat.parse ( " 31 Oktober 2007" );
        } catch (ParseException er) {
            er.printStackTrace ();
        }
        Film film5 = new Film (
                idFilm,
                "Paku Kuntilanak",
                tempDate,
                storeImageFile ( R.drawable.foto5 ),
                "Dewi Perssik\n" +
                        "Heather Storm\n" +
                        "Keith Foo\n" +
                        "Kiwil\n" +
                        "Rizky Mocil\n" +
                        "Hardi Fadhillah\n" +
                        "Edi Brokoli\n" +
                        "Nani Widjaja\n" +
                        "Baron Hermanto\n" +
                        "Cynthiara Alona",
                "Horror",
                "Film dimulai dengan kisah penangkapan Kuntilanak (Dewi Perssik) oleh sebuah pasangan Oca (Keith Foo) dan Sally (Heather Storm) yang selalu diganggu Kuntilanak tersebut. Kemudian dukun yang mereka sewa, Cahyo, berhasil menangkap Kuntilanak itu dan memaku kepalanya. Apabila kepala Kuntilanak dipaku, maka Kuntilanak tidak akan ganas seperti biasanya, dan apabila tidak dicabut, maka mayatnya akan membusuk seiring tubuhnya bersikap sebagai manusia biasa. Lalu, Oca bersama dengan Cahyo, mereka mengikat mayat Kunti di dalam koper, lalu dijatuhkan ke sungai.\n" +
                        "\n" +
                        "Tiga Pemburu Mayat; Sukun (Edi Brokoli), Obeng (Hardi Fadhillah), dan Odjie (Rizky Mocil) dengan segala keseharian mereka dalam memburu mayat sebagai pekerjaan yang dimanajeri oleh Pak Joko (Kiwil) yang selalu diteror mengenai masalah kelajangannya diumurnya yang semakin tua oleh sang Ibu (Nani Widjaja). Setelah frustasi dan tercebur kedalam sungai, Pak Joko yang nyaris tenggelam tertolong oleh koper berisi mayat Kunti yang tersangkut di bebatuan. Pak Jokopun melepas tali dan ikatan koper, yang membuat Kunti terbebas setelah Pak Joko kabur. Kuntipun mulai kembali mengganas. Kemudian, Kunti bertemu kembali dengan Pak Joko yang ingin pulang kerumah. Kuntipun mulai merayu Pak Joko dengan meminta dijadikan suaminya. Pak Joko tentu setuju, dan saat malamnya, ia tidak sadar telah mencabut paku yang ada di kepala Kunti, Kuntipun menjelma menjadi wujud hantunya kembali dan berhasil membunuh Cahyo. Kunti yang saat siang menjadi manusia biasa, bersikap ramah selama tinggal dengan Pak Joko dan mereka akan melangsungkan pernikahan.",
                "https://www.youtube.com/watch?v=gTa7zHT9PlY"
        );

        tambahFilm (film5,db );

    }
}
