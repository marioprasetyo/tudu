package app.trian.tudu.ui.component.task

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.content.res.Configuration.UI_MODE_NIGHT_NO
import android.content.res.Configuration.UI_MODE_NIGHT_YES
import android.widget.DatePicker
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.material.*
import androidx.compose.material3.IconToggleButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import app.trian.tudu.R
import app.trian.tudu.common.getNowMillis
import app.trian.tudu.common.hideKeyboard
import app.trian.tudu.common.showKeyboard
import app.trian.tudu.data.local.Category
import app.trian.tudu.data.local.Task
import app.trian.tudu.data.local.Todo
import app.trian.tudu.ui.component.ItemAddTodo
import app.trian.tudu.ui.component.ItemTodo
import app.trian.tudu.ui.component.dialog.DropdownPickCategory
import app.trian.tudu.ui.theme.HexToJetpackColor
import app.trian.tudu.ui.theme.Inactivebackground
import app.trian.tudu.ui.theme.TuduTheme
import compose.icons.Octicons
import compose.icons.octicons.*
import org.joda.time.DateTime
import java.util.*


/**
 * Input new task
 * author Trian Damai
 * created_at 29/01/22 - 15.57
 * site https://trian.app
 */

@SuppressLint("MutableCollectionMutableState")
@Composable
fun BottomSheetInputNewTask(
    modifier: Modifier=Modifier,
    listCategory:List<Category> = emptyList(),
    onSubmit:(
        taskName:Task,
        todo:List<Todo>
    )->Unit={ _,_-> },
    onAddCategory: ()->Unit={}
){
    val ctx = LocalContext.current



    var showDropDownPickCategory by remember {
        mutableStateOf(false)
    }
    var taskName by remember {
        mutableStateOf(TextFieldValue(text = ""))
    }
    var categoryId by remember {
        mutableStateOf(Category(name = ctx.getString(R.string.no_category), created_at = 0, updated_at = 0, color = HexToJetpackColor.Blue))
    }
    var todos by remember {
        mutableStateOf<List<Todo>>(mutableListOf())
    }
    var deadline by remember {
        mutableStateOf<Long>(0)
    }

    var setReminder by remember {
        mutableStateOf(false)
    }

    val c = Calendar.getInstance()
    val year = c.get(Calendar.YEAR)
    val month = c.get(Calendar.MONTH)
    val day = c.get(Calendar.DAY_OF_MONTH)
    val datePickerDialog = DatePickerDialog(ctx,{
            _:DatePicker,year:Int,month:Int,day:Int->
            val date = DateTime(year,(month+1),day,0,0)
        deadline = date.millis
    },year,month,day)


    /**
     * before send into parent and save
     * validate first
     * */
    fun submit(){
        if(taskName.text.isBlank()){
            Toast.makeText(ctx,ctx.getString(R.string.blank_validation,"Task name"),Toast.LENGTH_LONG).show()
        }else{
            //prepare data before save
            val currentTime = getNowMillis()
            val task = Task(
                name=taskName.text,
                deadline=deadline,
                done=false,
                done_at=0,
                note="",
                color=HexToJetpackColor.Blue,
                secondColor=HexToJetpackColor.SecondBlue,
                reminder=setReminder,
                category_id=categoryId.categoryId,
                created_at=currentTime,
                updated_at=currentTime
            )

            //submit data
            onSubmit(task, todos)
            //clear all state to initial value each
            deadline = 0
            setReminder = false
            taskName = TextFieldValue(text = "")
            categoryId = Category(
                name = ctx.getString(R.string.no_category),
                updated_at = 0,
                created_at = 0,
                color = HexToJetpackColor.Blue
            )
            todos = mutableListOf()
        }
    }


    Box(
        modifier = modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .clip(
                RoundedCornerShape(
                    topStart = 10.dp,
                    topEnd = 10.dp
                )
            )
            .background(MaterialTheme.colorScheme.background)
    ) {
        Column(
            modifier = modifier
                .fillMaxWidth()

                .padding(
                    vertical = 16.dp,
                    horizontal = 16.dp
                )
                .align(Alignment.Center),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Top
        ) {
            TextField(
                modifier=modifier.fillMaxWidth(),
                value =taskName,
                onValueChange = {
                    taskName = it
                },
                shape = RoundedCornerShape(10.dp),
                colors = TextFieldDefaults.textFieldColors(
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent,
                    textColor = Color.Black,
                    cursorColor = Color.Black
                ),
                placeholder = {
                    Text(
                        text = stringResource(R.string.placeholder_input_new_task),
                        style = TextStyle(
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.Black
                        )
                    )
                },
                textStyle = TextStyle(
                    color = Color.Black,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                )

            )

            LazyColumn(content = {
                items(count=todos.size){
                    index: Int ->
                    ItemTodo(
                        todo = todos[index],
                        onDelete = {
                            todos = todos - todos[index]
                        },
                        onDone = {
                            todos[index].done = true
                        },
                        onChange = {
                            todos[index].name = it.name
                        }
                    )
                }
            })

            Row(
                modifier=modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
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
                            horizontal = 8.dp,
                            vertical = 4.dp
                        )
                        .clickable {
                            showDropDownPickCategory = true

                        }
                ){
                    DropdownPickCategory(
                        show = showDropDownPickCategory,
                        listCategory = listCategory,
                        onAddCategory = {
                            onAddCategory()
                        },
                        onHide = {
                            showDropDownPickCategory = false
                        },
                        onPick = {
                            categoryId = it
                        }
                    )
                    Text(
                        text = categoryId.name
                    )
                }

                IconToggleButton(
                    checked = deadline > 0,
                    onCheckedChange = {
                        datePickerDialog.show()
                    }
                ) {
                    Icon(
                        imageVector = Octicons.Calendar24,
                        contentDescription = "",
                        tint=if(deadline > 0) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onBackground
                    )
                }
                IconToggleButton(
                    checked = todos.isNotEmpty(),
                    onCheckedChange = {
                        val currentTime = getNowMillis()
                        todos = todos + Todo(
                            name = "",
                            done = false,
                            task_id = "",
                            created_at = currentTime,
                            updated_at = currentTime
                        )
                    }
                ) {
                    Icon(
                        imageVector = Octicons.GitMerge24,
                        contentDescription = "",
                        tint=if(todos.isNotEmpty()) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onBackground
                    )
                }
                IconToggleButton(
                    checked = setReminder,
                    onCheckedChange = {
                        setReminder = !setReminder
                        Toast.makeText(ctx,if(setReminder) "Reminder on!" else "Reminder off",Toast.LENGTH_LONG).show()
                    }
                ) {
                    Icon(
                        imageVector = Octicons.Clock24,
                        contentDescription = "",
                        tint=if(setReminder) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onBackground
                    )
                }
                IconToggleButton(
                    checked = false,
                    onCheckedChange = {
                        submit()
                    }
                ) {
                    Icon(
                        imageVector = Octicons.PaperAirplane24,
                        contentDescription = "",
                        tint=MaterialTheme.colorScheme.onBackground
                    )
                }
            }
        }

    }
}
@Preview(
    uiMode = UI_MODE_NIGHT_NO
)
@Preview(
    uiMode = UI_MODE_NIGHT_YES
)
@Composable
fun PreviewBottomSheetInputNewTask(){
    TuduTheme {
        BottomSheetInputNewTask()
    }
}