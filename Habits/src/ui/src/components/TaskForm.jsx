import {
    Box,
    Typography,
    TextField,
    Stack,
    IconButton,
    SwipeableDrawer,
    Paper,
    FormControlLabel,
    Checkbox,
} from '@mui/material';
import CheckCircleRoundedIcon from '@mui/icons-material/CheckCircleRounded';
import RemoveCircleRoundedIcon from '@mui/icons-material/RemoveCircleRounded';
import AddCircleRoundedIcon from '@mui/icons-material/AddCircleRounded';
import RepeatRoundedIcon from '@mui/icons-material/RepeatRounded';
import EventRoundedIcon from '@mui/icons-material/EventRounded';
import { LocalizationProvider } from '@mui/x-date-pickers/LocalizationProvider';
import { AdapterDayjs } from '@mui/x-date-pickers/AdapterDayjs';
import { DatePicker } from '@mui/x-date-pickers/DatePicker';
import dayjs from 'dayjs';
import { useEffect, useRef } from 'react';
import { motion } from 'framer-motion';
import { useForm, Controller } from 'react-hook-form';
import { styled } from '@mui/material/styles';
import { useTaskActions, useTaskState } from "../hooks/taskHooks";
import { useSettingsState, useSettingsActions } from '../hooks/settingsHooks';

const FormButton = styled(IconButton)({
    transition: 'all 0.3s ease',
    '&:hover': {
        transform: 'scale(1.30)',
    }
})

const RoutineCheckBox = styled(Checkbox)(({ theme }) => ({
    color: theme.palette.custom.grey,
    '&.Mui-checked': {
        color: theme.palette.custom.darkgreen,
    }
}));

const DateCheckBox = styled(Checkbox)(({ theme }) => ({
    color: theme.palette.custom.grey,
    '&.Mui-checked': {
        color: theme.palette.custom.darkgreen,
    }
}));

const ButtonBox = styled(Box)(({ theme }) => ({
    border: '1px solid',
    borderColor: theme.palette.primary.main,
    borderRadius: '1rem',
    paddingRight: '1rem',
    backgroundColor: theme.palette.primary.main,
}));

function TaskForm() {
    const taskState = useTaskState();
    const settingsState = useSettingsState();

    const { handleSaveTask, handleDeleteTask } = useTaskActions(); 
    const { handleOpenTaskForm } = useSettingsActions();

    const task = taskState.currentTask;
    const isOpen = settingsState.openTaskForm;
    const isCreate = task == null;
    const dialogTitle = (isCreate ? "Add a new task" : "Edit task");


    const { handleSubmit, control, watch, reset }
        = useForm({
            defaultValues: {
                id: "",
                title: "",
                description: "",
                dueDate: dayjs(),
                isCompleted: false,
                hasDueDate: false,
                isRoutineTask: false,
            }
        })
    const hasDueDate = watch('hasDueDate');
    const isRoutineTask = watch('isRoutineTask');
    const titleRef = useRef(null);
    const descRef = useRef(null);

    useEffect(() => {
        reset({
            id: task?.id || "",
            title: task?.title || "",
            description: task?.description || "",
            dueDate: task?.dueDate ? dayjs(task.dueDate) : dayjs(),
            isCompleted: task?.isCompleted || false,
            hasDueDate: task?.dueDate ? true : false,
            isRoutineTask: task?.routineDetailsResponse?.isRoutineTask || false,
        })
    }, [isCreate, task, isOpen]);

    useEffect(() => {
        if (isOpen && titleRef.current) {
            titleRef.current.focus();
        }
    }, [isOpen])

    const handleClose = () => {
        handleOpenTaskForm(false);
    }

    const handleOnSave = async (data) => {
        const dueDateString = hasDueDate ? data.dueDate.format('YYYY-MM-DD') : '';
        const newTask = {
            id: data.id,
            title: data.title,
            description: data.description,
            dueDate: dueDateString,
            isCompleted: data.isCompleted,
            routineDetailsResponse: {
                isRoutineTask: isRoutineTask,
            }
        }
        // Add/Update task
        handleSaveTask(isCreate, newTask);

        // Close dialog
        handleClose();
    }

    const handleOnDelete = () => {
        handleDeleteTask(task.id);
        handleClose();
    }

    const renderTextField = (name, label, inputRef, required) => (
        <Controller
            name={name}
            control={control}
            render={({ field }) => (
                <TextField
                    required={required}
                    inputRef={inputRef}
                    id={name}
                    name={name}
                    label={label}
                    variant='outlined'
                    value={field.value}
                    onChange={field.onChange}
                />
            )}
        />
    )

    const renderRoutineCheckBox = () => (
        <Controller
            name="isRoutineTask"
            control={control}
            render={({ field }) => (
                <ButtonBox>
                    <FormControlLabel
                        sx={{color: 'secondary.main'}}
                        control={
                            <RoutineCheckBox 
                                checked={field.value}
                                onChange={field.onChange}
                                icon={<RepeatRoundedIcon/>}
                                checkedIcon={<RepeatRoundedIcon />}
                            />
                        }
                        label="Routine"
                        labelPlacement='start'
                    />
                </ButtonBox>
            )}
        />
    )

    const renderDateCheckBox = () => (
        <Controller 
            name='hasDueDate'
            control={control}
            render={({ field }) => (
                <ButtonBox>
                    <FormControlLabel 
                        sx={{color: 'secondary.main'}}
                        control={
                            <DateCheckBox 
                                checked={field.value}
                                onChange={field.onChange}
                                icon={<EventRoundedIcon />}
                                checkedIcon={<EventRoundedIcon />}
                            />
                        }
                        label="Date"
                        labelPlacement='start'
                    />
                </ButtonBox>
            )}
        />
    )

    const renderDatePicker = () => (
        <Controller
            name="dueDate"
            control={control}
            render={({ field }) => (
                <LocalizationProvider dateAdapter={AdapterDayjs}>
                    <DatePicker
                        label="Due Date"
                        value={field.value}
                        sx={{ width: "10rem" }}
                        minDate={dayjs()}
                        onChange={(newDate) => field.onChange(newDate)}
                        slotProps={{
                            openPickerButton: {
                                sx: {
                                    color: 'primary.main',
                                }
                            }
                        }}
                    />
                </LocalizationProvider>
            )}
        />
    )

    return (
        <Box>
            <SwipeableDrawer
                anchor='bottom'
                open={isOpen}
                onClose={handleClose}
            >
                <Paper
                    component="form"
                    onSubmit={handleSubmit(handleOnSave)}
                >
                    <Box
                        component={motion.div}
                        layout
                        transition={{
                            layout: {
                                type: 'tween',
                                ease: 'easeInOut'
                            }
                        }}
                        sx={{ padding: '2rem' }}
                    >
                        <Box display="flex" justifyContent="space-between">
                            <FormButton
                                aria-label='delete-task'
                                sx={{ color: 'custom.darkred' }}
                                onClick={handleOnDelete}
                            >
                                {!isCreate && (<RemoveCircleRoundedIcon fontSize='large' />)}
                            </FormButton>
                            <Typography variant='h6'>
                                {dialogTitle}
                            </Typography>
                            <FormButton
                                type='submit'
                                aria-label='update-task'
                                sx={{ color: 'primary.main' }}
                            >
                                {isCreate ? <AddCircleRoundedIcon fontSize='large' /> : <CheckCircleRoundedIcon fontSize='large' />}
                            </FormButton>
                        </Box>
                        <Stack spacing={2} paddingTop='1rem'>
                            {renderTextField('title', 'Title', titleRef, true)}
                            {renderTextField('description', 'Description', descRef, false)}
                        </Stack>
                        <Box display='flex' marginTop='1rem' gap={1}>
                            {renderRoutineCheckBox()}
                            {renderDateCheckBox()}
                        </Box>
                        {hasDueDate &&
                            <Box display='flex' marginTop='1rem'>
                                {renderDatePicker()}
                            </Box>
                        }
                    </Box>
                </Paper>
            </SwipeableDrawer>
        </Box>
    );

}
export default TaskForm;