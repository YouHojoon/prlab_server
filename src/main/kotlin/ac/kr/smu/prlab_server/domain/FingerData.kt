package ac.kr.smu.prlab_server.domain

import ac.kr.smu.prlab_server.enum.MeasurementTarget
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.PrimaryKeyJoinColumn
import java.sql.Timestamp

@Entity
@PrimaryKeyJoinColumn(name="id")
class FingerData(
    bpm: Int,
    SpO2: Int,
    RR: Int,
    stressIndex: Int,
    measurementDate: Timestamp,
    confidence: Float,

    @Column(updatable = false, nullable = false)
    val SYS: Int,

    @Column(updatable = false, nullable = false)
    val DIA: Int,

    @Column(updatable = false, nullable = false)
    val bloodSugar: Int
): MeasurementData(bpm,SpO2,RR,stressIndex, measurementDate, confidence){

}