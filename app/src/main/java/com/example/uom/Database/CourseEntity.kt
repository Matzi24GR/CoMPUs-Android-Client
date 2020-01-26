package com.example.uom.Database

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "courses_table")
data class Course(val titleStr:String, val profsStr:String, val urlStr:String, val semesterInt:Int, val activeBool:Boolean, val codeStr:String) {

    var Title = titleStr
    var Profs = profsStr
    var Url = urlStr
    var Semester = semesterInt
    var Active = activeBool
    var Code = codeStr

    @PrimaryKey
    var id = Code+Title
}