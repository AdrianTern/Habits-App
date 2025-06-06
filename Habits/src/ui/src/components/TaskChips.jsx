import { Box, Chip, } from '@mui/material';
import TodayRoundedIcon from '@mui/icons-material/TodayRounded';
import WatchLaterRoundedIcon from '@mui/icons-material/WatchLaterRounded';
import ErrorRoundedIcon from '@mui/icons-material/ErrorRounded';
import AssignmentRoundedIcon from '@mui/icons-material/AssignmentRounded';
import RepeatRoundedIcon from '@mui/icons-material/RepeatRounded';
import { useState, useEffect } from 'react';
import { motion } from 'framer-motion';

function TaskChips({ onSelect, taskCount }){
    const taskGroups = [
        { key: 'all', label: "All tasks", icon: <AssignmentRoundedIcon /> },
        { key: 'today', label: "Today's tasks", icon: <TodayRoundedIcon />},
        { key:'upcoming', label: "Upcoming tasks", icon: <WatchLaterRoundedIcon /> },
        { key:'overdue', label: "Overdue tasks", icon: <ErrorRoundedIcon /> },
        { key: 'routine', label: "Routines", icon: <RepeatRoundedIcon /> },
    ];

    const [selectedGroupKey, setSelectedGroupKey] = useState(taskGroups[0].key)

    const handleOnSelect = (key) => {
        setSelectedGroupKey(key);
        onSelect(key);
    }

    const taskBadgeCount = (key) => {
        let count = 0;
        const maxCount = 99;

        if(key === 'all') count = taskCount.allCount;
        else if(key === 'today') count = taskCount.todayCount;
        else if(key === 'upcoming') count = taskCount.upcomingCount;
        else if(key === 'overdue') count = taskCount.overdueCount;
        else if(key === 'routine') count = taskCount.routineCount;

        count = Math.min(count, maxCount);
   
        if(count === maxCount) return `${maxCount}+`;
        else if(count === 0) return '';
        
        return count;
    }

    useEffect(() => {
        onSelect(selectedGroupKey);
    }, [])

    return(
        <Box
            component={motion.div}
            layout
            gap={1}
            sx={{
                display: 'flex',
                justifyContent: 'center',
                alignItems: 'center',
                marginBottom: '1.5rem',
                flexWrap: 'wrap',
                maxWidth: { xs: '100vw', sm: '70vw', md: '50vw'},
            }}
        >
            {taskGroups.map(taskGroup => (
                <Chip 
                    key={taskGroup.key} 
                    label={`${taskGroup.label} ${taskBadgeCount(taskGroup.key)}`} 
                    icon={taskGroup.icon}
                    color={ selectedGroupKey === taskGroup.key ? 'primary' : 'default' } 
                    onClick={(key) => handleOnSelect(taskGroup.key)}
                    sx={{padding:0.5}}
                />
            ))}   
        </Box>
    )
}

export default TaskChips;