package com.fauzan.post4

import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.snackbar.Snackbar

class MainActivity : AppCompatActivity() {

    private lateinit var db: DatabaseHelper
    private lateinit var etNama: EditText
    private lateinit var etNIK: EditText
    private lateinit var etKabupaten: EditText
    private lateinit var etKecamatan: EditText
    private lateinit var etDesa: EditText
    private lateinit var etRT: EditText
    private lateinit var etRW: EditText
    private lateinit var rgGender: RadioGroup
    private lateinit var rbLaki: RadioButton
    private lateinit var rbPerempuan: RadioButton
    private lateinit var spStatus: Spinner
    private lateinit var tvOutput: TextView
    private lateinit var btnSimpan: Button
    private lateinit var btnReset: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        db = DatabaseHelper(this)

        etNama = findViewById(R.id.etNama)
        etNIK = findViewById(R.id.etNIK)
        etKabupaten = findViewById(R.id.etKabupaten)
        etKecamatan = findViewById(R.id.etKecamatan)
        etDesa = findViewById(R.id.etDesa)
        etRT = findViewById(R.id.etRT)
        etRW = findViewById(R.id.etRW)
        rgGender = findViewById(R.id.rgGender)
        rbLaki = findViewById(R.id.rbLaki)
        rbPerempuan = findViewById(R.id.rbPerempuan)
        spStatus = findViewById(R.id.spStatus)
        tvOutput = findViewById(R.id.tvOutput)
        btnSimpan = findViewById(R.id.btnSimpan)
        btnReset = findViewById(R.id.btnReset)

        val statusList = listOf("Belum Menikah", "Menikah", "Cerai")
        spStatus.adapter =
            ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, statusList)

        tampilkanData()

        btnSimpan.setOnClickListener { simpanData() }
        btnReset.setOnClickListener { hapusSemuaData() }
    }

    private fun simpanData() {
        val nama = etNama.text.toString().trim()
        val nik = etNIK.text.toString().trim()
        val kab = etKabupaten.text.toString().trim()
        val kec = etKecamatan.text.toString().trim()
        val desa = etDesa.text.toString().trim()
        val rt = etRT.text.toString().trim()
        val rw = etRW.text.toString().trim()
        val gender = when {
            rbLaki.isChecked -> "Laki-Laki"
            rbPerempuan.isChecked -> "Perempuan"
            else -> ""
        }
        val status = spStatus.selectedItem.toString()

        if (nama.isEmpty() || nik.isEmpty() || kab.isEmpty() || kec.isEmpty() ||
            desa.isEmpty() || rt.isEmpty() || rw.isEmpty() || gender.isEmpty()
        ) {
            Snackbar.make(btnSimpan, "Isi semua data!!", Snackbar.LENGTH_SHORT).show()
            return
        }

        val isInserted = db.insertWarga(nama, nik, kab, kec, desa, rt, rw, gender, status)
        if (isInserted) {
            Snackbar.make(btnSimpan, "Data Tersimpan!", Snackbar.LENGTH_SHORT).show()
            resetInput()
            tampilkanData()
        } else {
            Snackbar.make(btnSimpan, "Gagal menyimpan!", Snackbar.LENGTH_SHORT).show()
        }
    }

    private fun tampilkanData() {
        val dataList = db.getAllWarga()
        if (dataList.isEmpty()) {
            tvOutput.text = "Belum ada data warga."
        } else {
            val dataDenganNomor = dataList.mapIndexed { index, data ->
                "${index + 1}. $data"
            }.joinToString("\n\n")

            tvOutput.text = dataDenganNomor
        }
        for (data in dataList) {
            android.util.Log.d("DATA_WARGA", data)
        }
    }

    private fun resetInput() {
        etNama.text.clear()
        etNIK.text.clear()
        etKabupaten.text.clear()
        etKecamatan.text.clear()
        etDesa.text.clear()
        etRT.text.clear()
        etRW.text.clear()
        rgGender.clearCheck()
        spStatus.setSelection(0)
    }

    private fun hapusSemuaData() {
        db.deleteAllWarga()
        tampilkanData()
        Snackbar.make(btnReset, "Semua data sudah terhapus!", Snackbar.LENGTH_SHORT).show()
    }
}