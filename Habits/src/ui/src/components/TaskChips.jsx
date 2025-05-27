import { Typography, Box, Chip } from '@mui/material';
import TodayRoundedIcon from '@mui/icons-material/TodayRounded';
import WatchLaterRoundedIcon from '@mui/icons-material/WatchLaterRounded';
import ErrorRoundedIcon from '@mui/icons-material/ErrorRounded';
import AssignmentRoundedIcon from '@mui/icons-material/AssignmentRounded';
import { useState, useEffect } from 'react';
import { motion } from 'framer-motion';

function TaskChips({ onSelect }){
    const [taskGroups, setTaskGroups] = useState([
        { key: 'today', label: "Today's tasks", icon: <TodayRoundedIcon />},
        { key:'upcoming', label: "Upcoming tasks", icon: <WatchLaterRoundedIcon /> },
        { key:'overdued', label: "Overdued tasks", icon: <ErrorRoundedIcon /> },
        { key: 'all', label: "All tasks", icon: <AssignmentRoundedIcon /> },
    ]);

    const [selectedGroupKey, setSelectedGroupKey] = useState(taskGroups[0].key)

    const handleOnClick = (key) => {
        setSelectedGroupKey(key)
    }
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
                flexWrap: 'wrap-reverse',
                maxWidth: { xs: '100vw', sm: '70vw', md: '50vw'},
            }}
        >
            {taskGroups.map(taskGroup => (
                <Chip 
                    key={taskGroup.key} 
                    label={taskGroup.label} 
                    icon={taskGroup.icon}
                    color={ selectedGroupKey === taskGroup.key ? 'primary' : 'default' } 
                    onClick={(key) => handleOnClick(taskGroup.key)}/>
            ))}   
        </Box>
    )
}

export default TaskChips;