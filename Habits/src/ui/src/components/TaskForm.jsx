import {
    Box,
    Typography,
    TextField,
    Stack,
    IconButton,
    SwipeableDrawer,
    Paper,
    Switch,
    FormControlLabel,
} from '@mui/material';
import CheckCircleRoundedIcon from '@mui/icons-material/CheckCircleRounded';
import RemoveCircleRoundedIcon from '@mui/icons-material/RemoveCircleRounded';
import AddCircleRoundedIcon from '@mui/icons-material/AddCircleRounded';
import { LocalizationProvider } from '@mui/x-date-pickers/LocalizationProvider';
import { AdapterDayjs } from '@mui/x-date-pickers/AdapterDayjs';
import { DatePicker } from '@mui/x-date-pickers/DatePicker';
import dayjs from 'dayjs';
import { useEffect, useRef } from 'react';
import { motion } from 'framer-motion';
import { useForm, Controller } from 'react-hook-form';
import { styled } from '@mui/material/styles';

function TaskForm({ task, isOpen, onClose, onSave, onDelete }) {
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
            }
        })
    const hasDueDate = watch('hasDueDate');
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
        })
    }, [isCreate, task, isOpen]);

    useEffect(() => {
        if (isOpen && titleRef.current) {
            titleRef.current.focus();
        }
    }, [isOpen])

    const handleOnSave = async (data) => {
        const dueDateString = hasDueDate ? data.dueDate.format('YYYY-MM-DD') : '';
        const newTask = {
            id: data.id,
            title: data.title,
            description: data.description,
            dueDate: dueDateString,
            isCompleted: data.isCompleted
        }
        // Add/Update task
        onSave(isCreate, newTask);

        // Close dialog
        onClose();
    }

    const handleOnDelete = () => {
        onDelete(task.id);
        onClose();
    }

    const FormButton = styled(IconButton)({
        transition: 'all 0.3s ease',
        '&:hover': {
            transform: 'scale(1.30)',
        }
    })

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

    const renderDateSwitch = () => (
        <Controller
            name="hasDueDate"
            control={control}
            render={({ field }) => (
                <FormControlLabel
                    control={
                        <Switch
                            checked={field.value}
                            onChange={field.onChange}
                            sx={{
                                '& .MuiSwitch-thumb': {
                                    color: 'primary.main',
                                },
                                '& .MuiSwitch-switchBase + .MuiSwitch-track': {
                                    backgroundColor: 'custom.lightgrey',
                                    opacity: 1
                                },
                                '& .MuiSwitch-switchBase.Mui-checked + .MuiSwitch-track': {
                                    backgroundColor: 'custom.darkred',
                                    opacity: 1
                                }
                            }}
                        />
                    }
                    label="Date"
                    labelPlacement='start'
                />
            )}
        />
    );

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
                onClose={onClose}
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
                                sx={{ color: 'custom.red' }}
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
                                sx={{ color: 'black' }}
                            >
                                {isCreate ? <AddCircleRoundedIcon fontSize='large' /> : <CheckCircleRoundedIcon fontSize='large' />}
                            </FormButton>
                        </Box>
                        <Stack spacing={2} paddingTop='1rem'>
                            {renderTextField('title', 'Title', titleRef, true)}
                            {renderTextField('description', 'Description', descRef, false)}
                        </Stack>
                        <Box display='flex' marginTop='1rem'>
                            {renderDateSwitch()}
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