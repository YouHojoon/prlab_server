package ac.kr.smu.prlab_server.service

import ac.kr.smu.prlab_server.domain.MeasurementData
import ac.kr.smu.prlab_server.domain.User
import ac.kr.smu.prlab_server.enums.Metric
import ac.kr.smu.prlab_server.enums.Period
import ac.kr.smu.prlab_server.util.MetricData
import ac.kr.smu.prlab_server.util.RecentData
import java.util.Date

interface MeasurementDataService {
    fun findById(id: Long): MeasurementData?
    fun findRecentData(user: User): RecentData
    fun deleteById(id: Long)
    fun findMetricDatasByPeriodAndDate(metric:Metric, period: Period, date: Date): List<MetricData>
}