import TaskItem from "./TaskItem";
import { Box, List, Typography, Button } from '@mui/material';
import { styled } from '@mui/material/styles';
import AddRoundedIcon from '@mui/icons-material/AddRounded';
import { motion } from 'framer-motion';
import { useTaskActions, useTaskState } from "../hooks/taskHooks";

const CenterBox = styled(Box)({
    display: 'flex',
    flexDirection: 'column',
    justifyContent: 'center',
    alignItems: 'center',
    marginTop: '20%',
})

function TaskList() {
    const state = useTaskState();
    const { handleSetCurrentTask, handleOpenTaskForm } = useTaskActions();
    const isEmpty = !state.tasks || state.tasks.length === 0;

    const TaskItems = () => {
        return (
            state.tasks.map(task => (
                <TaskItem
                    key={task.id}
                    task={task}
                    onEdit={() => {
                        handleSetCurrentTask(task);
                        handleOpenTaskForm(true);
                    }}
                />
            ))
        )
    }

    const EmptyTaskBox = () => {
        return (
            <CenterBox>
                <Typography variant="h5" sx={{ color: 'custom.darkgrey' }} gutterBottom>
                    No task found
                </Typography>
                <Button
                    variant="contained"
                    size="small"
                    startIcon={<AddRoundedIcon />}
                    onClick={() => handleOpenTaskForm(true)}
                >
                    Add new task
                </Button>
            </CenterBox>
        )
    }
    
    return (
        <>
            <List
                component={motion.ul}
                layout
                sx={{
                    maxHeight: { xs: '60vh', sm: '100vh' },
                    maxWidth: { xs: '90vw', sm: '100vw' },
                    overflowY: { xs: 'auto', sm: 'hidden' },
                    overflowX: { xs: 'auto', sm: 'hidden' },
                }}>
                {isEmpty ? <EmptyTaskBox /> : <TaskItems />}
            </List>
        </>
    );
}

export default TaskList;