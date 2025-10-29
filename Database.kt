package com.fauzan.post4

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class DatabaseHelper(context: Context) :
    SQLiteOpenHelper(context, "data_warga.db", null, 1) {

    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL(
            "CREATE TABLE warga (" +
                    "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    "nama TEXT, nik TEXT, kabupaten TEXT, kecamatan TEXT, desa TEXT, " +
                    "rt TEXT, rw TEXT, gender TEXT, status TEXT)"
        )
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS warga")
    }

    fun insertWarga(
        nama: String, nik: String, kabupaten: String, kecamatan: String,
        desa: String, rt: String, rw: String, gender: String, status: String
    ): Boolean {
        val db = writableDatabase
        val values = ContentValues()
        values.put("nama", nama)
        values.put("nik", nik)
        values.put("kabupaten", kabupaten)
        values.put("kecamatan", kecamatan)
        values.put("desa", desa)
        values.put("rt", rt)
        values.put("rw", rw)
        values.put("gender", gender)
        values.put("status", status)
        val result = db.insert("warga", null, values)
        return result != -1L
    }

    fun getAllWarga(): List<String> {
        val db = readableDatabase
        val cursor = db.rawQuery("SELECT nama, nik, kabupaten, kecamatan, desa, rt, rw, gender, status FROM warga", null)
        val list = mutableListOf<String>()
        if (cursor.moveToFirst()) {
            do {
                val nama = cursor.getString(cursor.getColumnIndexOrThrow("nama"))
                val nik = cursor.getString(cursor.getColumnIndexOrThrow("nik"))
                val kab = cursor.getString(cursor.getColumnIndexOrThrow("kabupaten"))
                val kec = cursor.getString(cursor.getColumnIndexOrThrow("kecamatan"))
                val desa = cursor.getString(cursor.getColumnIndexOrThrow("desa"))
                val rt = cursor.getString(cursor.getColumnIndexOrThrow("rt"))
                val rw = cursor.getString(cursor.getColumnIndexOrThrow("rw"))
                val gender = cursor.getString(cursor.getColumnIndexOrThrow("gender"))
                val status = cursor.getString(cursor.getColumnIndexOrThrow("status"))

                val data = "$nama ($gender) - $status\nNIK: $nik\nAlamat: RT $rt/RW $rw, $desa, $kec, $kab"
                list.add(data)
            } while (cursor.moveToNext())
        }
        cursor.close()
        return list
    }

    fun deleteAllWarga() {
        val db = writableDatabase
        db.execSQL("DELETE FROM warga")
    }
}