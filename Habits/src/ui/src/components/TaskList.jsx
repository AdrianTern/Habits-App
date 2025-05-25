import TaskItem from "./TaskItem";
import { Box, List, IconButton } from '@mui/material';
import AddCircleIcon from '@mui/icons-material/AddCircle';
import { useState, useEffect } from 'react';
import TaskForm from "./TaskForm";

function TaskList({ tasks, onToggle, onSave, onDelete }){
    const [openTaskDialog, setOpenTaskDialog] = useState(false);
    const [currentTask, setCurrentTask] = useState(null);

    const handleOpenDialog = () => {
        setOpenTaskDialog(true);
    };

    const handleCloseDialog = () => {
        setOpenTaskDialog(false);
    }

    return(
        <Box>
            <List 
                className="task-list"
                sx={{
                    maxHeight: { xs: '60vh', sm: '70vh', md: '80vh'},
                    maxWidth: {xs: '90vw', sm: '60vw', md: '50vw'},
                    overflowY: 'auto',
                    overflowX: 'hidden',
                }}>
                {tasks.map(task => (
                    <TaskItem 
                        key={task.id} 
                        task={task} 
                        onToggle={onToggle}
                        onEdit={()=>{
                            setCurrentTask(task);
                            handleOpenDialog();
                        }}
                    />
                ))}
            </List>
            <Box sx={{display: 'flex', justifyContent:'center'}}>
                <IconButton 
                    aria-label="add task"
                    onClick={() => {
                        setCurrentTask(null);
                        handleOpenDialog();
                    }} 
                    sx={{ 
                        color: 'black',
                        transition: 'all 0.3s ease',
                        '&:hover': {
                            transform: 'scale(1.30)',
                        }
                    }}>
                    <AddCircleIcon fontSize='large'/>
                </IconButton>
            </Box>
            <TaskForm task={currentTask} isOpen={openTaskDialog} onClose={handleCloseDialog} onSave={onSave} onDelete={onDelete}/>         
        </Box>
    );
}

export default TaskList;