package app.trian.tudu.ui.component.task

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.material.TextFieldDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import app.trian.tudu.R
import app.trian.tudu.common.toReadableDate
import app.trian.tudu.data.local.Category
import app.trian.tudu.data.local.Task
import app.trian.tudu.data.local.Todo
import app.trian.tudu.ui.component.ItemAddTodo
import app.trian.tudu.ui.component.ItemTodo
import app.trian.tudu.ui.theme.HexToJetpackColor
import app.trian.tudu.ui.theme.Inactivebackground
import app.trian.tudu.ui.theme.TuduTheme
import compose.icons.Octicons
import compose.icons.octicons.*
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

/**
 * Screen Detail Task
 * author Trian Damai
 * created_at 30/01/22 - 22.31
 * site https://trian.app
 */
@Composable
fun ScreenDetailTask(
    modifier: Modifier=Modifier,
    task: Task,
    category: Category?,
    todo:List<Todo> = emptyList(),
    updateTask:(task:Task)->Unit={},
    addNewTodo:()->Unit={},
    updateTodo:(todo:Todo)->Unit={},
    deleteTodo:(todo:Todo)->Unit={}
) {
    val scope = rememberCoroutineScope()
    var taskName by remember {
        mutableStateOf(TextFieldValue(text = task.name))
    }
    var deadline by remember {
        mutableStateOf(task.deadline)
    }
    var reminder by remember {
        mutableStateOf(task.reminder)
    }

    fun update(){
        scope.launch {
            delay(800)
            updateTask(task.apply { name=taskName.text })
        }
    }

        Column(
            modifier=modifier.fillMaxSize(),
            horizontalAlignment = Alignment.Start,
            verticalArrangement = Arrangement.Top
        ) {
            Box(modifier = modifier.padding(horizontal = 20.dp)) {
                Row(
                    modifier = modifier
                        .clip(
                            RoundedCornerShape(
                                topStart = 10.dp,
                                topEnd = 10.dp,
                                bottomStart = 10.dp,
                                bottomEnd = 10.dp
                            )
                        )
                        .background(MaterialTheme.colorScheme.primary)
                        .padding(
                            vertical = 10.dp,
                            horizontal = 16.dp
                        )
                        .clickable { }
                ) {
                    Text(text = category?.name ?: stringResource(id = R.string.no_category))
                    Icon(imageVector = Octicons.ChevronDown16, contentDescription = "")
                }
            }
            LazyColumn(content = {
                item {

                    Spacer(modifier = modifier.height(16.dp))
                    TextField(
                        modifier=modifier.fillMaxWidth(),
                        value = taskName,
                        onValueChange = {
                            taskName = it
                            update()
                        },
                        placeholder={
                            Text(
                                text = taskName.text,
                                style = TextStyle(
                                    fontSize = 36.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color.Black
                                )
                            )
                        },
                        textStyle = TextStyle(
                            fontSize = 36.sp,
                            fontWeight = FontWeight.Bold
                        ),
                        colors = TextFieldDefaults.textFieldColors(
                            focusedIndicatorColor = Color.Transparent,
                            unfocusedIndicatorColor = Color.Transparent,
                            textColor = Color.Black,
                            backgroundColor = Color.Transparent,
                        )
                    )
                }
                item {
                    Box (
                        modifier=modifier.padding(
                            horizontal = 20.dp
                        )
                    ){
                        Spacer(modifier = modifier.height(10.dp))
                        Text(
                            text = stringResource(R.string.label_incomplete_todo),
                            style= TextStyle(
                                fontSize = 20.sp,
                                fontWeight = FontWeight.SemiBold
                            )
                        )
                    }
                }
                items(todo.filter { !it.done }){
                        data->
                    Box (
                        modifier=modifier.padding(
                            horizontal = 20.dp
                        )
                    ) {
                        ItemTodo(
                            todo = data,
                            onDone = {
                                updateTodo(it)
                            },
                            onChange = {
                                updateTodo(it)
                            },
                            onDelete = {
                                deleteTodo(it)
                            }
                        )
                    }
                }
                item {
                    Box (
                        modifier=modifier.padding(
                            horizontal = 20.dp
                        )
                    ) {
                        ItemAddTodo {
                            addNewTodo()
                        }
                    }
                }
                item {
                    Box (
                        modifier=modifier.padding(
                            horizontal = 20.dp
                        )
                    ) {
                        Spacer(modifier = modifier.height(10.dp))
                        Text(
                            text = stringResource(R.string.label_completed_todo),
                            style = TextStyle(
                                fontSize = 20.sp,
                                fontWeight = FontWeight.SemiBold
                            )
                        )
                        Spacer(modifier = modifier.height(10.dp))
                    }
                }
                items(todo.filter { it.done }){
                        data->
                    Box (
                        modifier=modifier.padding(
                            horizontal = 20.dp
                        )
                    ) {
                        ItemTodo(
                            todo = data,
                            onDone = {
                                updateTodo(it)
                            },
                            onChange = {
                                updateTodo(it)
                            },
                            onDelete = {
                                deleteTodo(it)
                            }
                        )
                    }
                }
                item {
                    Spacer(modifier = modifier.height(16.dp))
                    Divider()
                    Spacer(modifier = modifier.height(16.dp))
                    Box (
                        modifier=modifier.padding(
                            horizontal = 20.dp
                        )
                    ) {
                        Row(
                            modifier=modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row {
                                Icon(imageVector = Octicons.Calendar16, contentDescription = "")
                                Spacer(modifier = modifier.width(6.dp))
                                Text(text = stringResource(R.string.label_due_date))
                            }
                            Row(
                                modifier= modifier
                                    .clip(
                                        RoundedCornerShape(
                                            topStart = 10.dp,
                                            topEnd = 10.dp,
                                            bottomStart = 10.dp,
                                            bottomEnd = 10.dp
                                        )
                                    )
                                    .background(Inactivebackground)
                                    .padding(
                                        horizontal = 10.dp,
                                        vertical = 2.dp
                                    )
                            ) {
                                Text(text = task.created_at.toReadableDate())
                            }
                        }
                    }
                    Spacer(modifier = modifier.height(16.dp))
                }
                item {
                    Divider()
                    Spacer(modifier = modifier.height(16.dp))
                    Box (
                        modifier=modifier.padding(
                            horizontal = 20.dp
                        )
                    ) {
                        Row(
                            modifier=modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row {
                                Icon(imageVector = Octicons.Clock16, contentDescription = "")
                                Spacer(modifier = modifier.width(6.dp))
                                Text(text = stringResource(R.string.label_reminder))
                            }
                            Row(
                                modifier= modifier
                                    .clip(
                                        RoundedCornerShape(
                                            topStart = 10.dp,
                                            topEnd = 10.dp,
                                            bottomStart = 10.dp,
                                            bottomEnd = 10.dp
                                        )
                                    )
                                    .background(Inactivebackground)
                                    .padding(
                                        horizontal = 10.dp,
                                        vertical = 2.dp
                                    )
                            ) {
                                Text(text = if(task.reminder) stringResource(R.string.reminder_yes) else stringResource(
                                                                    R.string.reminder_no)
                                                                )
                            }
                        }
                    }
                    Spacer(modifier = modifier.height(16.dp))
                }
                item {
                    Divider()
                    Spacer(modifier = modifier.height(16.dp))
                    Box (
                        modifier=modifier.padding(
                            horizontal = 20.dp
                        )
                    ) {
                        Row(
                            modifier=modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.Top
                        ) {
                            Row(
                                horizontalArrangement = Arrangement.Start,
                                verticalAlignment = Alignment.Top
                            ) {
                                Icon(imageVector = Octicons.Note16, contentDescription = "")
                                Spacer(modifier = modifier.width(6.dp))
                                Column(
                                    modifier=modifier.fillMaxWidth(fraction = 0.8f)
                                ) {
                                    Text(text = stringResource(R.string.label_note))
                                    Text(text = task.note)
                                }
                            }
                            Row(
                                horizontalArrangement = Arrangement.End,
                                verticalAlignment = Alignment.Top,
                                modifier= modifier
                                    .clip(
                                        RoundedCornerShape(
                                            topStart = 10.dp,
                                            topEnd = 10.dp,
                                            bottomStart = 10.dp,
                                            bottomEnd = 10.dp
                                        )
                                    )
                                    .background(Inactivebackground)
                                    .padding(
                                        horizontal = 10.dp,
                                        vertical = 2.dp
                                    )
                            ) {
                                Text(text = stringResource(R.string.btn_add_note))
                            }
                        }
                    }
                    Spacer(modifier = modifier.height(16.dp))
                    Divider()
                }
            })
        }

}

@Preview
@Composable
fun PreviewScreenDetailTask() {
    TuduTheme {
        ScreenDetailTask(
            task = Task(
                name = "Ini tuh task tau",
                note = "",
                done = false,
                deadline = 0,
                done_at = 0,
                category_id="",
                reminder=false,
                color = HexToJetpackColor.SecondBlue,
                secondColor = HexToJetpackColor.SecondBlue,
                created_at = 0,
                updated_at = 0
            ),
            category = Category(
                name = "No Category",
                created_at = 0,
                updated_at = 0,
                color = HexToJetpackColor.Blue
            )
        )
    }
}