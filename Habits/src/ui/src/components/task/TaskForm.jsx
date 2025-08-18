import {
    Box,
    Typography,
    TextField,
    Stack,
    SwipeableDrawer,
    Paper,
    FormControlLabel,
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
import { useTasks, useTaskState } from "../../hooks/taskHooks";
import{ AnimateButton, InputCheckBox, ButtonBox } from '../../styles/StyledComponents';
import { STRINGS } from '../../constants/strings';

const TaskForm = () => {
    // Get state from task context and relevant task actions
    const state = useTaskState();
    const { saveTask, deleteTask, openTaskForm } = useTasks(); 

    const task = state.currentTask;
    const isOpen = state.openTaskForm;
    const isCreate = task == null;
    const dialogTitle = (isCreate ? STRINGS.DIALOG_TITLE.ADD : STRINGS.DIALOG_TITLE.EDIT);

    // useForm hook to manage state of form inputs
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
        });
    const hasDueDate = watch('hasDueDate');
    const isRoutineTask = watch('isRoutineTask');
    const titleRef = useRef(null);
    const descRef = useRef(null);
    
    // Re-initialize form input states on change of task 
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
    }, [task]);

    // Focus on title input when form is opened
    useEffect(() => {
        if (isOpen && titleRef.current) {
            titleRef.current.focus();
        }
    }, [isOpen]);

    // Handler for closing form
    const handleClose = () => {
        openTaskForm(false);
    };

    // Handler to submit form data
    const handleOnSave = async (data) => {
        const dueDateString = hasDueDate ? data.dueDate.format(STRINGS.DATE_FORMAT) : '';
        const newTask = {
            id: data.id,
            title: data.title,
            description: data.description,
            dueDate: dueDateString,
            isCompleted: data.isCompleted,
            routineDetailsResponse: {
                isRoutineTask: isRoutineTask,
            }
        };
        // Add/Update task
        saveTask(isCreate, newTask);

        // Close form
        handleClose();
    };

    // Handler to delete task
    const handleOnDelete = () => {
        deleteTask(task.id);
        handleClose();
    };

    // Function to render text input field
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
                    aria-label={label}
                    variant='outlined'
                    value={field.value}
                    onChange={field.onChange}
                />
            )}
        />
    );

    // Function to render RoutineCheckBox
    const renderRoutineCheckBox = () => (
        <Controller
            name="isRoutineTask"
            control={control}
            render={({ field }) => (
                <ButtonBox>
                    <FormControlLabel
                        sx={{color: 'secondary.main'}}
                        control={
                            <InputCheckBox 
                                aria-label='set task as routine'
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
    );

    // Function to render DateCheckBox
    const renderDateCheckBox = () => (
        <Controller 
            name='hasDueDate'
            control={control}
            render={({ field }) => (
                <ButtonBox>
                    <FormControlLabel 
                        sx={{color: 'secondary.main'}}
                        control={
                            <InputCheckBox 
                                aria-label='set task with due date'
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
    );

    // Function to render DatePicker to set due date
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
    );

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
                            {/* Delete Button */}
                            <AnimateButton
                                aria-label='delete task'
                                sx={{ color: 'custom.darkred' }}
                                onClick={handleOnDelete}
                            >
                                {!isCreate && (<RemoveCircleRoundedIcon fontSize='large' />)}
                            </AnimateButton>

                            <Typography variant='h6'>
                                {dialogTitle}
                            </Typography>

                            {/* Create/Submit Button */}
                            <AnimateButton
                                type='submit'
                                aria-label='save task'
                                sx={{ color: 'primary.main' }}
                            >
                                {isCreate ? <AddCircleRoundedIcon fontSize='large' /> : <CheckCircleRoundedIcon fontSize='large' />}
                            </AnimateButton>
                        </Box>

                        {/* Task Title Input and Task Description Input */}
                        <Stack spacing={2} paddingTop='1rem'>
                            {renderTextField('title', 'Title', titleRef, true)}
                            {renderTextField('description', 'Description', descRef, false)}
                        </Stack>

                        {/* Routine Checkbox and Due Date Checkbox */}
                        <Box display='flex' marginTop='1rem' gap={1}>
                            {renderRoutineCheckBox()}
                            {renderDateCheckBox()}
                        </Box>

                        {/* Date Picker */}
                        {hasDueDate &&
                            <Box display='flex' marginTop='1.3rem'>
                                {renderDatePicker()}
                            </Box>
                        }
                    </Box>
                </Paper>
            </SwipeableDrawer>
        </Box>
    );
};
export default TaskForm;