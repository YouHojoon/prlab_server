package ac.kr.smu.prlab_server.repository

import ac.kr.smu.prlab_server.domain.MeasurementData
import ac.kr.smu.prlab_server.enum.MeasurementTarget
import org.springframework.data.domain.Slice
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.repository.PagingAndSortingRepository
import org.springframework.data.domain.Pageable

interface MeasurementDataRepository: JpaRepository<MeasurementData,Long>{
    fun findByTargetOrderByMeasurementDateDesc(target: MeasurementTarget, pageable: Pageable): Slice<MeasurementData>
}