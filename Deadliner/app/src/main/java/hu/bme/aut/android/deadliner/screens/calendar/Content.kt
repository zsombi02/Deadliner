package hu.bme.aut.android.deadliner.screens.calendar

import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import hu.bme.aut.android.deadliner.data.calendar.CalendarUiModel

@Composable
fun Content(data: CalendarUiModel,
            onDateClickListener: (CalendarUiModel.Date) -> Unit,
) {
    LazyRow {
        items(items = data.visibleDates) { date ->
            ContentItem(
                date = date,
                onDateClickListener
            )
        }
    }
}