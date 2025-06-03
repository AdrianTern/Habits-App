import {
    ListItem,
    ListItemButton,
    ListItemIcon,
    ListItemText,
    IconButton,
    Checkbox,
    Typography,
    Box,
} from '@mui/material';
import EditNoteRoundedIcon from '@mui/icons-material/EditNoteRounded';
import RadioButtonUncheckedRoundedIcon from '@mui/icons-material/RadioButtonUncheckedRounded';
import RadioButtonCheckedRoundedIcon from '@mui/icons-material/RadioButtonCheckedRounded';
import ErrorRoundedIcon from '@mui/icons-material/ErrorRounded';
import RepeatRoundedIcon from '@mui/icons-material/RepeatRounded';
import { styled, useTheme } from '@mui/material/styles';
import { useState } from 'react';
import dayjs from 'dayjs';
import { motion } from 'framer-motion';

function TaskItem({ task, onToggle, onEdit }) {
    const theme = useTheme();
    const [isCompleted, setIsCompleted] = useState(task.isCompleted);
    const isRoutine = task?.routineDetailsResponse?.isRoutineTask || false;
    const isOverdued = !isRoutine && !isCompleted && dayjs(task.dueDate).isBefore(dayjs(), 'day');


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

    const PrettyListItemButton = styled(ListItemButton)({
        transition: 'all 0.5s ease',
        '&:hover': {
            transform: 'scale(1.02)',
        },
    });

    const TaskInfoText = styled(Typography)({
        fontSize: '0.7rem',
        [theme.breakpoints.up('sm')]: {
            fontSize: '0.8rem'
        },
        color: theme.palette.custom.darkgrey,
    })

    const TaskInfo = () => {
        return (
            <Box>
                <TaskInfoText>
                    {task.description}
                </TaskInfoText>
                <TaskInfoText>
                    {task.dueDate}
                </TaskInfoText>
            </Box>
        )
    }

    return (
        <motion.li
            layout
            key={task.id}
            className="task-item"
        >
            <Box display='flex'>
                <PrettyListItemButton onClick={handleToggle} dense>
                    <ListItemIcon>
                        <BlackCheckbox
                            className="task-iscomplete"
                            edge="start"
                            checked={isCompleted}
                            onClick={handleToggle}
                            icon={<RadioButtonUncheckedRoundedIcon />}
                            checkedIcon={<RadioButtonCheckedRoundedIcon />}
                            color='black'
                            disableRipple
                            sx={{
                                '& .MuiSvgIcon-root': {
                                    fontSize: { xs: '1rem', sm: '1.5rem' },
                                }
                            }} />
                    </ListItemIcon>
                    <Box>
                        <ListItemText
                            className="task-title"
                            sx={{
                                textDecoration: isCompleted ? 'line-through' : 'none',
                                color: isCompleted ? 'gray' : 'black'
                            }}
                            primary={
                                <Box display="flex">
                                    <Typography
                                        variant='h6'
                                        noWrap='false'
                                        sx={{
                                            fontSize: { xs: '0.9rem', sm: '1rem' },
                                            mr: 1
                                        }}
                                    >
                                        {task.title}
                                    </Typography >
                                    {isOverdued && <ErrorRoundedIcon fontSize='small' sx={{ color: 'custom.darkred', mt: 0.2 }} />}
                                    {isRoutine && <RepeatRoundedIcon fontSize='small' sx={{ color: 'custom.darkgreen', mt: 0.2 }} />}
                                </Box>
                            }
                        />
                        {!isRoutine && <TaskInfo/>}
                    </Box>
                </PrettyListItemButton>
                <IconButton
                        edge="end"
                        aria-label="edit"
                        size='medium'
                        onClick={onEdit}
                        sx={{
                            color: 'black',
                            visibility: isCompleted ? 'hidden' : 'visible',
                            marginRight: '0.5rem',
                            transition: 'all 0.3s ease',
                                '&:hover': {
                                    transform: 'scale(1.10)',
                                },      
                        }}>
                        <EditNoteRoundedIcon fontSize='medium' />
                </IconButton>
            </Box>
        </motion.li>
    );
}

export default TaskItem;