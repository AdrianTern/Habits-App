import TaskItem from "./TaskItem";
import { Box, List, IconButton, Typography, duration } from '@mui/material';
import AddCircleIcon from '@mui/icons-material/AddCircle';
import { useState } from 'react';
import TaskForm from "./TaskForm";
import { motion } from 'framer-motion';
import { useTaskState } from "../hooks/taskHooks";

function TaskList() {
    const [openTaskDialog, setOpenTaskDialog] = useState(false);
    const [currentTask, setCurrentTask] = useState(null);
    const state = useTaskState();

    const handleOpenDialog = () => {
        setOpenTaskDialog(true);
    };

    const handleCloseDialog = () => {
        setOpenTaskDialog(false);
    }

    const renderTaskItems = () => {
        const isEmpty = !state.tasks || state.tasks.length === 0;

        if (!isEmpty) {
            return (
                state.tasks.map(task => (
                    <TaskItem
                        key={task.id}
                        task={task}
                        onEdit={() => {
                            setCurrentTask(task);
                            handleOpenDialog();
                        }}
                    />
                ))
            )
        } else {
            return (
                <Box
                    key={isEmpty}
                    component={motion.div}
                    layout
                    sx={{
                        display: 'flex',
                        justifyContent: 'center',
                        alignItems: 'center',
                    }}
                >
                    <Typography variant="h6" sx={{ color: 'custom.darkgrey' }}>
                        You have 0 tasks!
                    </Typography>
                </Box>
            )
        }
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
                {renderTaskItems()}
            </List>

            <Box

                sx={{
                    display: 'flex',
                    justifyContent: 'center'
                }}
            >
                <IconButton
                    aria-label="add task"
                    onClick={() => {
                        setCurrentTask(null);
                        handleOpenDialog();
                    }}
                    sx={{
                        color: 'primary.main',
                        transition: 'all 0.3s ease',
                        '&:hover': {
                            transform: 'scale(1.30)',
                        }
                    }}>
                    <AddCircleIcon fontSize='large' />
                </IconButton>
            </Box>
            <TaskForm task={currentTask} isOpen={openTaskDialog} onClose={handleCloseDialog} />
        </>
    );
}

export default TaskList;