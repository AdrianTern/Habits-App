import TaskItem from "./TaskItem";
import { Box, List, IconButton, Typography, duration } from '@mui/material';
import AddCircleIcon from '@mui/icons-material/AddCircle';
import { useState } from 'react';
import TaskForm from "./TaskForm";
import { motion } from 'framer-motion';

function TaskList({ tasks, appSettings, onToggle, onSave, onDelete, darkMode }) {
    const [openTaskDialog, setOpenTaskDialog] = useState(false);
    const [currentTask, setCurrentTask] = useState(null);

    const handleOpenDialog = () => {
        setOpenTaskDialog(true);
    };

    const handleCloseDialog = () => {
        setOpenTaskDialog(false);
    }

    const Items = () => {
        const isEmpty = !tasks || tasks.length === 0;

        if(!isEmpty){
            return (
                tasks.map(task => (
                        <TaskItem
                            key={task.id}
                            task={task}
                            onToggle={onToggle}
                            appSettings={appSettings}
                            onEdit={() => {
                                setCurrentTask(task);
                                handleOpenDialog();
                            }}
                            darkMode={darkMode}
                        />
                ))
            ) 
        } else{
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
                    <Typography variant="h6" sx={{color: 'custom.darkgrey'}}>
                        You have 0 tasks!
                    </Typography>
                </Box>
            )
        }
    }
    return (
        <Box>
            <List
                component={motion.ul}
                layout
                className="task-list"
                sx={{
                    maxHeight: { xs: '60vh', sm: '100vh' },
                    maxWidth: { xs: '90vw', sm: '100vw' },
                    overflowY: { xs: 'auto', sm: 'hidden' },
                    overflowX: { xs: 'auto', sm: 'hidden' },
                }}>
                {Items()}
            </List>
            <Box 
                component={motion.div}
                layout
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
            <TaskForm task={currentTask} isOpen={openTaskDialog} onClose={handleCloseDialog} onSave={onSave} onDelete={onDelete} />
        </Box>
    );
}

export default TaskList;