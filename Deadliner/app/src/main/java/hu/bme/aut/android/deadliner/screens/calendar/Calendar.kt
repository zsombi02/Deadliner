package hu.bme.aut.android.deadliner.screens.calendar

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import hu.bme.aut.android.deadliner.data.calendar.CalendarDataSource
import java.time.LocalDate
import java.util.Date

@Composable
fun Calendar(modifier: Modifier = Modifier,
             dataSource: CalendarDataSource = CalendarDataSource(),
             onDateSelected: (LocalDate) -> Unit // Add this parameter
) {
    //val calendarUiModel = dataSource.getData(lastSelectedDate = dataSource.today),
    var calendarUiModel by remember { mutableStateOf(dataSource.getData(lastSelectedDate = dataSource.today)) }


    Column(modifier = modifier.fillMaxSize()) {
        Header(
            data = calendarUiModel,
            onPrevClickListener = { startDate ->
                // refresh the CalendarUiModel with new data
                // by get data with new Start Date (which is the startDate-1 from the visibleDates)
                val finalStartDate = startDate.minusDays(1)
                calendarUiModel = dataSource.getData(startDate = finalStartDate, lastSelectedDate = calendarUiModel.selectedDate.date)
            },
            onNextClickListener = { endDate ->
                // refresh the CalendarUiModel with new data
                // by get data with new Start Date (which is the endDate+2 from the visibleDates)
                val finalStartDate = endDate.plusDays(2)
                calendarUiModel = dataSource.getData(startDate = finalStartDate, lastSelectedDate = calendarUiModel.selectedDate.date)
            }
        )
        Content(data = calendarUiModel, onDateClickListener = { date ->
            // refresh the CalendarUiModel with new data
            // by changing only the `selectedDate` with the date selected by User
            calendarUiModel = calendarUiModel.copy(
                selectedDate = date,
                visibleDates = calendarUiModel.visibleDates.map {
                    it.copy(
                        isSelected = it.date.isEqual(date.date)
                    )
                }
            )
            onDateSelected(date.date)
        })
    }

}

@Preview(showSystemUi = true)
@Composable
fun CalendarAppPreview() {
    Calendar(
        modifier = Modifier.padding(16.dp),
        onDateSelected = {}
    )
}