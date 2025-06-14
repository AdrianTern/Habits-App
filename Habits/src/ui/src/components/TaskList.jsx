import TaskItem from "./TaskItem";
import { Box, List, Typography, Button, CircularProgress } from '@mui/material';
import { styled } from '@mui/material/styles';
import AddRoundedIcon from '@mui/icons-material/AddRounded';
import { useTasks } from "../hooks/taskHooks";

// Styled container to align child in the middle
const CenterBox = styled(Box)({
    display: 'flex',
    flexDirection: 'column',
    justifyContent: 'center',
    alignItems: 'center',
})

// Styled button to add new task from the list
const AddButton = styled(Button)(({ theme }) => ({
    borderRadius: '20px',
    paddingLeft: '10px',
    paddingRight: '10px',
    backgroundColor: theme.palette.custom.violet,
}));

function TaskList() {
    // Get relevant task actions
    const { tasks, tasksLoading, errorFetching, tasksErrorObj, setCurrentTask, openTaskForm } = useTasks();

    // Handler to edit a task
    const handleOnEdit = (task) => () => {
        setCurrentTask(task)
        openTaskForm(true)
    }

    // Function to render list of task items, or a fallback page when there is no task available
    const renderTaskItems = () => {
        const isEmpty = !tasks || tasks.length === 0;

        if(tasksLoading){
            return (
                <CenterBox>
                    <CircularProgress color="inherit" />
                </CenterBox>
            )
        }

        if (!isEmpty) {
            return (
                tasks.map(task => (
                    <TaskItem key={task.id} task={task} onEdit={handleOnEdit(task)}/>
                ))
            )
        } else {
            return (
                <CenterBox>
                    <Typography variant="h5" sx={{ color: 'custom.darkgrey' }} gutterBottom>
                        {errorFetching ? tasksErrorObj : 'No task found'}
                    </Typography>
                    {!errorFetching && <AddButton
                        aria-label="add new task"
                        variant="contained"
                        size="small"
                        startIcon={<AddRoundedIcon />}
                        onClick={() => openTaskForm(true)}
                    >
                        Add new task
                    </AddButton>}
                </CenterBox>
            )
        }
    }

    return (
        <>
            <List
                sx={{
                    maxHeight: { xs: '60vh', sm: '100vh' },
                    maxWidth: { xs: '90vw', sm: '100vw' },
                    overflowY: { xs: 'auto', sm: 'hidden' },
                    overflowX: { xs: 'auto', sm: 'hidden' },
                }}>
                {renderTaskItems()}
            </List>
        </>
    );
}

export default TaskList;