import TaskItem from "./TaskItem";
import { Box, List, Typography } from '@mui/material';
import { motion } from 'framer-motion';
import { useTaskActions, useTaskState } from "../hooks/taskHooks";
import { useSettingsActions } from "../hooks/settingsHooks";

function TaskList() {
    const state = useTaskState();
    const { handleSetCurrentTask } = useTaskActions();
    const { handleOpenTaskForm } = useSettingsActions();

    const renderTaskItems = () => {
        const isEmpty = !state.tasks || state.tasks.length === 0;

        if (!isEmpty) {
            return (
                state.tasks.map(task => (
                    <TaskItem
                        key={task.id}
                        task={task}
                        onEdit={() => {
                            handleSetCurrentTask(task);
                            handleOpenTaskForm(true);
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
        </>
    );
}

export default TaskList;