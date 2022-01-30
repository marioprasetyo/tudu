package app.trian.tudu.ui.pages.dashbboard

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import app.trian.tudu.common.Routes
import app.trian.tudu.common.gridItems
import app.trian.tudu.common.hideKeyboard
import app.trian.tudu.data.local.Category
import app.trian.tudu.ui.component.AppbarHome
import app.trian.tudu.ui.component.DialogFormCategory
import app.trian.tudu.ui.component.task.BottomSheetInputNewTask
import app.trian.tudu.ui.component.ItemTaskGrid
import app.trian.tudu.ui.component.ItemTaskRow
import app.trian.tudu.ui.component.header.HeaderTask
import app.trian.tudu.ui.theme.TuduTheme
import app.trian.tudu.viewmodel.TaskViewModel
import compose.icons.Octicons
import compose.icons.octicons.Plus16
import kotlinx.coroutines.launch

@ExperimentalComposeUiApi
@ExperimentalMaterialApi
@Composable
fun PageHome(
    modifier:Modifier=Modifier,
    router: NavHostController,
){
    val taskViewModel = hiltViewModel<TaskViewModel>()
    val listTask by taskViewModel.listTask.observeAsState(initial = emptyList())
    val listCategory by taskViewModel.listCategory.observeAsState(initial = listOf(Category(
        name = "All",
        created_at = 0,
        updated_at = 0
    )))

    val scope = rememberCoroutineScope()
    val ctx = LocalContext.current

    val modalBottomSheetState = rememberModalBottomSheetState(
        initialValue = ModalBottomSheetValue.Hidden,
        skipHalfExpanded = false,
        confirmStateChange = {
            if(it == ModalBottomSheetValue.Hidden){
                ctx.hideKeyboard()
            }
            true
        }
    )
    var listType by remember {
        mutableStateOf(HeaderTask.ROW)
    }
    var shouldShowDialogAddCategory by remember {
        mutableStateOf(false)
    }



    LaunchedEffect(key1 = Unit, block = {
        taskViewModel.getListTask()
        taskViewModel.getListCategory()
    })

    DialogFormCategory(
        show = shouldShowDialogAddCategory,
        onHide = {
            shouldShowDialogAddCategory = false
        },
        onSubmit = {
            taskViewModel.addNewCategory(it)
        }
    )

    BasePagesDashboard(
        modalBottomSheetState=modalBottomSheetState,
        topAppbar = {
            AppbarHome(
                dataCategory = listCategory,
                onCategoryManagement = {
                    router.navigate(Routes.CATEGORY)
                },
                onSelectCategory = {
                    if(it.name == "All"){
                        taskViewModel.getListTask()
                    }else {
                        taskViewModel.getListTaskByCategory(it.categoryId)
                    }
                }
            )
        },
        sheetContent={
            BottomSheetInputNewTask(
                listCategory = listCategory,
                onSubmit = {
                    task,todo->
                    taskViewModel.addNewTask(task,todo)
                    scope.launch {
                        modalBottomSheetState.hide()
                        ctx.hideKeyboard()
                    }
                },
                onAddCategory = {
                    shouldShowDialogAddCategory = true
                }
            )
        },
        router = router,
    ) {
        Box(modifier = modifier.padding(bottom = 60.dp)) {
            LazyColumn(
                modifier = modifier
                    .fillMaxSize()
                    .padding(horizontal = 30.dp)
            ) {
                item{
                    HeaderTask(
                        onListTypeChange = {
                            listType = it
                        }
                    )
                }
                when(listType){
                    HeaderTask.GRID -> {
                        gridItems(listTask, columnCount = 2) {
                                data  ->
                            ItemTaskGrid(task = data)
                        }
                    }
                    HeaderTask.ROW -> {
                        items(listTask) { data ->
                            ItemTaskRow(
                                task = data,
                                onDone = {

                                },
                                onDetail = {
                                    router.navigate("${Routes.DETAIL_TASK}/${it.taskId}")
                                }
                            )
                        }
                    }
                }

            }
            FloatingActionButton(
                backgroundColor=MaterialTheme.colorScheme.primary,
                modifier = modifier
                    .align(Alignment.BottomEnd),
                onClick = {
                    scope.launch {
                        modalBottomSheetState.show()
                    }
                }
            ) {
                Icon(
                    imageVector = Octicons.Plus16,
                    contentDescription = "",
                    tint = MaterialTheme.colorScheme.onPrimary
                )
            }
        }
    }
}

@ExperimentalComposeUiApi
@ExperimentalMaterialApi
@Preview
@Composable
fun PreviewPageHome(){
    TuduTheme {
        PageHome(router = rememberNavController())
    }
}