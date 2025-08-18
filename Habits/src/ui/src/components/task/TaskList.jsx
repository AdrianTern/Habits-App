import TaskItem from "./TaskItem";
import { List, Typography, CircularProgress } from '@mui/material';
import AddRoundedIcon from '@mui/icons-material/AddRounded';
import LinkOffRoundedIcon from '@mui/icons-material/LinkOffRounded';
import { useTasks } from "../../hooks/taskHooks";
import { CenterBox } from '../../styles/StyledComponents';
import { PrimaryButton } from '../../styles/StyledComponents';

const TaskList = () => {
    // Get relevant task actions
    const { tasks, tasksLoading, tasksError, setCurrentTask, openTaskForm } = useTasks();

    // Handler to edit a task
    const handleOnEdit = (task) => () => {
        setCurrentTask(task)
        openTaskForm(true)
    };

    return (
        <>
            <List
                sx={{
                    maxHeight: { xs: '60vh', sm: '100vh' },
                    maxWidth: { xs: '90vw', sm: '100vw' },
                    overflowY: { xs: 'auto', sm: 'hidden' },
                    overflowX: { xs: 'auto', sm: 'hidden' },
                }}>

                {/* Indicating tasks are fetching */}
                {tasksLoading && (
                    <CenterBox>
                        <CircularProgress color="inherit" />
                    </CenterBox>
                )}

                {/* Indicate failed to fetch tasks from API */}
                {!tasksLoading && tasksError && (
                    <CenterBox>
                        <LinkOffRoundedIcon sx={{ color: 'custom.darkgrey', fontSize: '3rem' }} />
                        <Typography variant="h5" sx={{ color: 'custom.darkgrey' }} gutterBottom>
                            Failed to fetch tasks
                        </Typography>
                    </CenterBox>
                )}

                {/* Shown when there is no task found */}
                {!tasksLoading && !tasksError && tasks?.length === 0 && (
                    <CenterBox>
                        <Typography variant="h5" sx={{ color: 'custom.darkgrey' }} gutterBottom>
                            No task found
                        </Typography>
                        <PrimaryButton
                            aria-label="add new task"
                            variant="contained"
                            size="small"
                            startIcon={<AddRoundedIcon />}
                            onClick={() => openTaskForm(true)}
                        >
                            Add new task
                        </PrimaryButton>
                    </CenterBox>
                )}

                {/* Show task items */}
                {!tasksLoading && !tasksError && tasks && (
                    tasks.map(task => (
                        <TaskItem key={task.id} task={task} onEdit={handleOnEdit(task)} />
                    ))
                )}
            </List>
        </>
    );
};

export default TaskList;