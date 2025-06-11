import { Box, Chip, } from '@mui/material';
import TodayRoundedIcon from '@mui/icons-material/TodayRounded';
import WatchLaterRoundedIcon from '@mui/icons-material/WatchLaterRounded';
import ErrorRoundedIcon from '@mui/icons-material/ErrorRounded';
import AssignmentRoundedIcon from '@mui/icons-material/AssignmentRounded';
import RepeatRoundedIcon from '@mui/icons-material/RepeatRounded';
import { useState, useEffect } from 'react';
import { useTaskActions, useTaskState } from '../hooks/taskHooks';

function TaskChips() {
    const taskGroups = [
        { key: 'all', label: "All tasks", icon: <AssignmentRoundedIcon /> },
        { key: 'today', label: "Today's tasks", icon: <TodayRoundedIcon /> },
        { key: 'upcoming', label: "Upcoming tasks", icon: <WatchLaterRoundedIcon /> },
        { key: 'overdue', label: "Overdue tasks", icon: <ErrorRoundedIcon /> },
        { key: 'routine', label: "Routines", icon: <RepeatRoundedIcon /> },
    ];

    const [selectedGroupKey, setSelectedGroupKey] = useState(taskGroups[0].key)

    const state = useTaskState();
    const { handleChangeTaskChip } = useTaskActions();

    const handleOnSelect = (key) => {
        setSelectedGroupKey(key);
        handleChangeTaskChip(key);
    }

    const taskBadgeCount = (key) => {
        let count = 0;
        const maxCount = 99;

        if (key === 'all') count = state.taskCount.allCount;
        else if (key === 'today') count = state.taskCount.todayCount;
        else if (key === 'upcoming') count = state.taskCount.upcomingCount;
        else if (key === 'overdue') count = state.taskCount.overdueCount;
        else if (key === 'routine') count = state.taskCount.routineCount;

        count = Math.min(count, maxCount);

        if (count === maxCount) return `${maxCount}+`;
        else if (count === 0) return '';

        return count;
    }

    useEffect(() => {
        handleChangeTaskChip(selectedGroupKey);
    }, [])

    return (
        <Box
            gap={1}
            sx={{
                display: 'flex',
                justifyContent: 'center',
                alignItems: 'center',
                marginBottom: '1.5rem',
                flexWrap: 'wrap',
                maxWidth: { xs: '100vw', sm: '70vw', md: '50vw' },
            }}
        >
            {taskGroups.map(taskGroup => (
                <Chip
                    key={taskGroup.key}
                    label={`${taskGroup.label} ${taskBadgeCount(taskGroup.key)}`}
                    color='' // to force icon to use fallback color
                    icon={taskGroup.icon}
                    onClick={() => handleOnSelect(taskGroup.key)}
                    sx={{
                        padding: 0.2,
                        color: selectedGroupKey === taskGroup.key ? 'custom.black' : 'custom.white',
                        backgroundColor: selectedGroupKey === taskGroup.key ? 'custom.violet' : 'custom.black',
                        '&:hover': {
                            color: selectedGroupKey === taskGroup.key ? 'primary.main' : 'secondary.main',
                            backgroundColor: 'custom.violet',
                        }
                    }}
                />
            ))}
        </Box>
    )
}

export default TaskChips;