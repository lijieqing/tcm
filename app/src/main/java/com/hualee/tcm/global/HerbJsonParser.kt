package com.hualee.tcm.global

import android.util.Log
import com.hualee.tcm.App
import com.hualee.tcm.db.HerbEntity
import org.json.JSONArray
import org.json.JSONObject

object HerbJsonParser {

    fun parseVersion(json: String): Long {
        return try {
            val version = JSONObject(json)
            version.optLong("version", -1L)
        } catch (e: Exception) {
            Log.d(App.TAG, "parse json failed: $e")
            -1
        }
    }


    fun parseToHerbEntity(json: String): List<HerbEntity> {
        val list = ArrayList<HerbEntity>()
        try {
            val rootArray = JSONArray(json)
            val len = rootArray.length()
            for (i in 0 until len) {
                val herbObj = rootArray.getJSONObject(i)
                val name = herbObj.getString("名称")
                val type = herbObj.getString("类别")
                val subType = herbObj.getString("子类别")
                val source = herbObj.getString("来源")
                val feature = herbObj.getString("性能特点")
                val effect = herbObj.getString("功效")
                val property = herbObj.getString("性味归经")

                val imageArray = herbObj.getJSONArray("图像")
                val imageList = imageArray.join("#")

                val indicationArray = herbObj.getJSONArray("主治")
                val indications = indicationArray.join("#")

                val combinationArray = herbObj.getJSONArray("配伍")
                val combinationList = ArrayList<JSONObject>()
                for (i1 in 0 until combinationArray.length()) {
                    combinationList.add(combinationArray.getJSONObject(i1))
                }
                val combination = combinationList.joinToString("#") { it.toString() }

                val herb = HerbEntity(
                    name = name,
                    type = type,
                    subType = subType,
                    source = source,
                    property = property,
                    effect = effect,
                    feature = feature,
                    imageList = imageList,
                    indications = indications,
                    combination = combination,
                )
                list.add(herb)
            }
        } catch (e: Exception) {
            Log.d(App.TAG, "parse json failed: $e")
        }

        return list
    }
}