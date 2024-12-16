package ie.setu.domain.repository.water


import ie.setu.domain.water.WaterLogAndStat
import ie.setu.domain.db.water.WaterGoalLogAndStats
import ie.setu.domain.db.water.WaterGoals
import ie.setu.utils.mapToWaterLogAndStats
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.transactions.transaction
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.deleteWhere
import org.jetbrains.exposed.sql.insert
import org.joda.time.DateTime


class WaterGoalLogAndStatsDAO {

    fun save(waterGoalLogAndStat: WaterLogAndStat): Int? {
        return transaction {
            val waterGoal = WaterGoals
                .selectAll()
                .firstOrNull { it[WaterGoals.userId] == waterGoalLogAndStat.userId }

            if (waterGoal != null) {
                val targetWaterValue = waterGoal[WaterGoals.waterTarget]

                val targetMetValue  = waterGoalLogAndStat.actualWaterIntake >= targetWaterValue
                val shortfallValue= if (waterGoalLogAndStat.actualWaterIntake < targetWaterValue) (targetWaterValue - waterGoalLogAndStat.actualWaterIntake) else 0
                val recommendationsValue= if (targetMetValue)  "You have done it, keep it up" else "Try to drink water even if you are not thirsty"

                val waterGoalLogId = WaterGoalLogAndStats.insert {
                    it[userId] = waterGoalLogAndStat.userId
                    it[waterGoalId] = waterGoalLogAndStat.waterGoalId
                    it[actualWaterIntake ] = waterGoalLogAndStat.actualWaterIntake
                    it[targetMet ] = targetMetValue
                    it[shortfall ] = shortfallValue
                    it[recommendations ] = recommendationsValue
                    it[entryTime] = DateTime.now()
                } get WaterGoalLogAndStats.id

                waterGoalLogId
            } else {
                null
            }
        }
    }


    fun findByUserId(userId: Int): List<WaterLogAndStat>{
        return transaction {
            WaterGoalLogAndStats
                .selectAll().where { WaterGoalLogAndStats.userId eq userId}
                .map { mapToWaterLogAndStats(it) }
        }
    }

    fun deleteByWaterGoalId (waterGoalId: Int): Int{
        return transaction{
            WaterGoalLogAndStats.deleteWhere { WaterGoalLogAndStats.waterGoalId eq waterGoalId }
        }
    }



}

