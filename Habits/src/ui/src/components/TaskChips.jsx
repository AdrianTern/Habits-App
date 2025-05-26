import { Typography, Box, Chip } from '@mui/material';
import { useState, useEffect } from 'react';

function TaskChips(){
    return(
        <Box gap={1}
            sx={{
                display: 'flex',
                justifyContent: 'center',
                alignItems: 'center',
                marginBottom: '1rem',
                flexWrap: 'wrap-reverse',
                maxWidth: { xs: '100vw', sm: '70vw', md: '50vw'},
            }}>
            <Chip label='All tasks'/>
            <Chip label='Upcoming tasks'/>
            <Chip label='Upcoming tasks'/>
            <Chip label='Upcoming tasks'/>
            <Chip label='Upcoming tasks'/>
            <Chip label='Upcoming tasks'/>
            <Chip label='Upcoming tasks'/>
            <Chip label='Upcoming tasks'/>
        </Box>
    )
}

export default TaskChips;