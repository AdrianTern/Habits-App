import { 
    ListItem, 
    ListItemButton, 
    ListItemIcon, 
    ListItemText, 
    IconButton, 
    Checkbox, 
    Typography 
} from '@mui/material';
import EditNoteRoundedIcon from '@mui/icons-material/EditNoteRounded';
import RadioButtonUncheckedRoundedIcon from '@mui/icons-material/RadioButtonUncheckedRounded';
import RadioButtonCheckedRoundedIcon from '@mui/icons-material/RadioButtonCheckedRounded';
import { styled } from '@mui/material/styles';
import { useState } from 'react';


function TaskItem({ task, onToggle, onEdit}) {
    const[isCompleted, setIsCompleted] = useState(task.isCompleted);

    const handleToggle = () => {
        setIsCompleted(!isCompleted);
        onToggle(task);
    };

    const BlackCheckbox = styled(Checkbox)({
        color: 'black',
        '&.Mui-checked': {
            color: 'black',
        }
    });

    const PrettyListItem = styled(ListItem)({
        transition: 'all 0.5s ease',
        '&:hover': {
            transform: 'scale(1.02)',
        },
    });

    const PrettyListItemButton = styled(ListItemButton)({
        transition: 'all 0.5s ease',
        '&:hover': {
            transform: 'scale(1.02)',
        },
    });

    return (
        <PrettyListItem 
            key={task.id}
            className="task-item"
            secondaryAction={!isCompleted && (
            <IconButton 
                edge="end" 
                aria-label="edit"
                onClick={onEdit} 
                sx={{ color: 'black'}}>
                <EditNoteRoundedIcon fontSize='medium'/>
            </IconButton>
            )}
        >
            <PrettyListItemButton onClick={handleToggle} dense>
                <ListItemIcon>
                    <BlackCheckbox
                        className="task-iscomplete"
                        edge="start"
                        checked={isCompleted}
                        onClick={handleToggle}
                        icon={<RadioButtonUncheckedRoundedIcon/>}
                        checkedIcon={<RadioButtonCheckedRoundedIcon/>}
                        color='black'
                        disableRipple/>
                </ListItemIcon>
                <ListItemText 
                    className="task-title" 
                    sx={{
                        textDecoration: isCompleted ? 'line-through' : 'none',
                        color: isCompleted ? 'gray' : 'black',
                    }}
                    primary={
                        <Typography variant='h6'>
                            {task.title}
                        </Typography>} 
                    secondary={
                        <>
                        <Typography variant='body2'>
                            {task.description}
                        </Typography>
                        <Typography variant='caption' color="text.secondary">
                            {task.dueDate}
                        </Typography>
                        </>
                    }
                    />
            </PrettyListItemButton>
        </PrettyListItem>
    );  
} 

export default TaskItem;