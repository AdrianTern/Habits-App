import {
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
import { styled } from '@mui/material/styles';
import { useState } from 'react';
import dayjs from 'dayjs';
import { motion } from 'framer-motion';
import { useSettingsState } from '../hooks/settingsHooks';
import { useTaskActions } from '../hooks/taskHooks';

const CompletionCheckbox = styled(Checkbox)(({ theme }) => ({
    color: theme.palette.primary.main,
}));

const TaskListItemButton = styled(ListItemButton)({
    transition: 'all 0.5s ease',
    '&:hover': {
        transform: 'scale(1.02)',
    },
});

const TaskInfoText = styled(Typography, {
    shouldForwardProp: prop => prop !== '$isCompleted' && prop !== '$darkMode'
})(({ theme, $darkMode, $isCompleted }) => ({
    fontSize: '0.7rem',
    [theme.breakpoints.up('sm')]: {
        fontSize: '0.8rem'
    },
    color: $isCompleted ? theme.palette.custom.darkgrey : $darkMode ? theme.palette.custom.lightgrey : theme.palette.custom.darkgrey,
    textDecoration: $isCompleted ? 'line-through' : 'none',
}));

function TaskItem({ task, onEdit }) {
    const state = useSettingsState();
    const { handleToggleTask } = useTaskActions();

    const [isCompleted, setIsCompleted] = useState(task.isCompleted);

    const isRoutine = task?.routineDetailsResponse?.isRoutineTask || false;
    const isOverdued = !isRoutine && !isCompleted && dayjs(task.dueDate).isBefore(dayjs(), 'day');

    const isShowDesc = state.isShowTaskDesc;
    const isShowDate = state.isShowTaskDate;
    const darkMode = state.darkMode;
    const isShowTaskInfo = isShowDesc || isShowDate;

    const handleToggle = () => {
        setIsCompleted(!isCompleted);
        handleToggleTask(task);
    };

    const TaskInfo = () => {
        return (
            <Box>
                <TaskInfoText $darkMode={darkMode} $isCompleted={isCompleted}>
                    {isShowDesc && task.description}
                </TaskInfoText>
                <TaskInfoText $darkMode={darkMode} $isCompleted={isCompleted}>
                    {isShowDate && task.dueDate}
                </TaskInfoText>
            </Box>
        )
    }

    return (
        <motion.li
            layout
            initial={{ opacity: 0, y: -20 }}
            animate={{ opacity: 1, y: 0 }}
            transition={{ duration: 0.3 }}
            key={task.id}
            className="task-item"
        >
            <Box display='flex'>
                <TaskListItemButton onClick={handleToggle} dense>
                    <ListItemIcon>
                        <CompletionCheckbox
                            className="task-iscomplete"
                            edge="start"
                            checked={isCompleted}
                            onClick={handleToggle}
                            icon={<RadioButtonUncheckedRoundedIcon />}
                            checkedIcon={<RadioButtonCheckedRoundedIcon />}
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
                                color: isCompleted ? 'custom.darkgrey' : 'primary.main'
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
                        {isShowTaskInfo && <TaskInfo />}
                    </Box>
                </TaskListItemButton>
                <IconButton
                    edge="end"
                    aria-label="edit"
                    size='medium'
                    onClick={onEdit}
                    sx={{
                        color: 'primary.main',
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