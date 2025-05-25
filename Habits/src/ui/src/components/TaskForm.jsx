import { 
    Box,
    Typography,
    TextField,
    Stack,
    IconButton, 
    SwipeableDrawer,
    Paper,
} from '@mui/material';
import CheckCircleRoundedIcon from '@mui/icons-material/CheckCircleRounded';
import RemoveCircleRoundedIcon from '@mui/icons-material/RemoveCircleRounded';
import AddCircleRoundedIcon from '@mui/icons-material/AddCircleRounded';
import { LocalizationProvider } from '@mui/x-date-pickers/LocalizationProvider';
import { AdapterDayjs } from '@mui/x-date-pickers/AdapterDayjs';
import { DatePicker } from '@mui/x-date-pickers/DatePicker';
import dayjs from 'dayjs';
import { useState, useEffect, useRef } from 'react';

function TaskForm({ task, isOpen, onClose, onSave, onDelete }){
    const isCreate = task == null;
    const dialogTitle = (isCreate ? "Add a new task" : "Edit task");
    
    const [id, setId] = useState("");
    const [title, setTitle] = useState("");
    const [description, setDescription] = useState("");
    const [dueDate, setDueDate] = useState(dayjs());
    const [isCompleted, setIsCompleted] = useState(false);

    const titleRef = useRef(null);

    useEffect(() => {
        if(isCreate){
            setTitle("");
            setDescription("");
            setDueDate(dayjs());
        }else{
            setId(task.id);
            setTitle(task.title);
            setDescription(task.description);
            setDueDate(dayjs(task.dueDate));
            setIsCompleted(task.isCompleted);
        }
    }, [isCreate, task]);

    useEffect(() => {
        if(isOpen && titleRef.current){
            titleRef.current.focus();
        }
    },  [isOpen])
    
    const handleOnTitleChange = (event) => {
        setTitle(event.target.value);
    }

    const handleOnDescChange = (event) => {
        setDescription(event.target.value);
    }

    const handleOnDueDateChange = (newDate) => {
        setDueDate(newDate);
    }

    const handleOnSave = async (event) => {
        event.preventDefault();
        const dueDateString = dueDate.format('YYYY-MM-DD');
        const newTask = {
            id,
            title,
            description,
            dueDate: dueDateString,
            isCompleted
        }
        // Add/Update task
        onSave(isCreate, newTask);

        // Close dialog
        onClose();
    }

    const handleOnDelete = () => {
        onDelete(id);
        onClose();
    }

    return(
        <Box>
            <SwipeableDrawer
                anchor='bottom'
                open={isOpen}
                onClose={onClose}
            >
                <Paper
                    component="form"
                    onSubmit={handleOnSave}
                >
                    <Box sx={{padding: '2rem'}}>
                        <Box display="flex" justifyContent="space-between">
                            <IconButton
                                aria-label='delete-task'
                                sx={{color: 'custom.red'}}
                                onClick={handleOnDelete}
                            >
                                {!isCreate && (<RemoveCircleRoundedIcon fontSize='large'/>)}
                            </IconButton>
                            <Typography variant='h6'>
                                {dialogTitle}
                            </Typography>
                            <IconButton 
                                type='submit'
                                aria-label='update-task'
                                sx={{color: 'black'}}
                            >
                                {isCreate ? <AddCircleRoundedIcon fontSize='large'/> : <CheckCircleRoundedIcon fontSize='large'/>}
                            </IconButton> 
                        </Box>
                        <Stack spacing={2} paddingTop='1rem'>
                            <TextField
                                onChange={handleOnTitleChange}
                                required
                                inputRef={titleRef}
                                id='title'
                                name='title'
                                label="Title"
                                value={title}
                                variant='outlined'
                            />
                            <TextField
                                onChange={handleOnDescChange}
                                fullWidth
                                id='description'
                                name='description'
                                label="Description"
                                value={description}
                                variant='outlined'
                            />
                        </Stack>
                        <Box display='flex' marginTop='1rem'>
                            <LocalizationProvider dateAdapter={AdapterDayjs}>
                                <DatePicker
                                    label="Due Date"
                                    value={dueDate}
                                    sx={{ width: "10rem" }}
                                    onChange={(newDate) => handleOnDueDateChange(newDate)}
                                />
                            </LocalizationProvider>
                        </Box>
                    </Box> 
                </Paper>
            </SwipeableDrawer>
        </Box>
    );

}
export default TaskForm;