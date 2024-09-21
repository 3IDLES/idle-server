import com.swm.idle.domain.jobposting.entity.jpa.JobPosting
import com.swm.idle.domain.jobposting.entity.jpa.JobPostingWeekday
import java.time.LocalDateTime

data class JobPostingPreviewDto(
    val jobPosting: JobPosting,
    val jobPostingWeekdays: List<JobPostingWeekday>,
    var distance: Int = 0,
    val applyTime: LocalDateTime?,
    val isFavorite: Boolean,
) {

    init {
        jobPostingWeekdays.distinctBy { it.weekday }
    }

    constructor(
        jobPosting: JobPosting,
        jobPostingWeekdays: List<JobPostingWeekday>,
        applyTime: LocalDateTime?,
    ) : this(jobPosting, jobPostingWeekdays.distinctBy { it.weekday }, 0, applyTime, true)

    constructor(
        jobPosting: JobPosting,
        jobPostingWeekdays: List<JobPostingWeekday>,
        applyTime: LocalDateTime?,
        isFavorite: Boolean,
    ) : this(jobPosting, jobPostingWeekdays.distinctBy { it.weekday }, 0, applyTime, isFavorite)

}
