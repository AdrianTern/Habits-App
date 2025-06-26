import TaskItem from "./TaskItem";
import { List, Typography, CircularProgress } from '@mui/material';
import AddRoundedIcon from '@mui/icons-material/AddRounded';
import { useTasks } from "../hooks/taskHooks";
import{ CenterBox } from '../styles/StyledComponents';
import{ PrimaryButton } from '../styles/StyledComponents';

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
                    {!errorFetching && <PrimaryButton
                        aria-label="add new task"
                        variant="contained"
                        size="small"
                        startIcon={<AddRoundedIcon />}
                        onClick={() => openTaskForm(true)}
                    >
                        Add new task
                    </PrimaryButton>}
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