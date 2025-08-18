import { Box, Chip, } from '@mui/material';
import TodayRoundedIcon from '@mui/icons-material/TodayRounded';
import CalendarMonthRoundedIcon from '@mui/icons-material/CalendarMonthRounded';
import ErrorRoundedIcon from '@mui/icons-material/ErrorRounded';
import AssignmentRoundedIcon from '@mui/icons-material/AssignmentRounded';
import RepeatRoundedIcon from '@mui/icons-material/RepeatRounded';
import { useTasks, useTaskState } from '../../hooks/taskHooks';
import { styled } from '@mui/material/styles';
import { STRINGS } from '../../constants/strings';
import { NUMBERS } from '../../constants/numbers';

// Styled chip for task chip
const TaskChip = styled(Chip, {
    shouldForwardProp: prop => prop !== '$isSelected'
})(({ theme, $isSelected }) => ({
    borderRadius: '20px',
    backdropFilter: 'blur(8px)',
    WebkitBackdropFilter: 'blur(8px)', // for Safari
    border: '1px solid ',
    borderColor: $isSelected ? theme.palette.custom.violet : theme.palette.custom.black,
    boxShadow: '0 4px 10px rgba(0, 0, 0, 0.3)',
    transition: 'all 0.3s ease',
    padding: 0.2,
    color: $isSelected ? theme.palette.custom.black : theme.palette.custom.white,
    backgroundColor: $isSelected ? theme.palette.custom.violet : theme.palette.custom.black,
    '&:hover': {
        color: theme.palette.custom.white,
        backgroundColor: theme.palette.custom.violet,
        transform: 'translateY(-2px)',
        boxShadow: '0 6px 14px rgba(0, 0, 0, 0.4)',
        borderColor: theme.palette.custom.violet,
    }
}));

// Styled container to hold chips
const ChipBox = styled(Box)(({ theme }) => ({
    display: 'flex',
    justifyContent: 'center',
    alignItems: 'center',
    marginBottom: '1.5rem',
    flexWrap: 'wrap',
    maxWidth: '100vw',

    [theme.breakpoints.up('sm')]: {
        maxwidth: '70vw',
    },

    [theme.breakpoints.up('md')]: {
        maxwidth: '50vw',
    },

}));

const TaskChips = () => {
    // Array of chips indicating task filter
    const taskGroups = [
        {
            key: STRINGS.TASK_FILTER_KEY.ALL,
            label: STRINGS.TASK_FILTER.ALL,
            icon: <AssignmentRoundedIcon />
        },
        {
            key: STRINGS.TASK_FILTER_KEY.TODAY,
            label: STRINGS.TASK_FILTER.TODAY, 
            icon: <TodayRoundedIcon />
        },
        { 
            key: STRINGS.TASK_FILTER_KEY.UPCOMING,
            label: STRINGS.TASK_FILTER.UPCOMING, 
            icon: <CalendarMonthRoundedIcon /> 
        },
        { 
            key: STRINGS.TASK_FILTER_KEY.OVERDUE,
            label: STRINGS.TASK_FILTER.OVERDUE, 
            icon: <ErrorRoundedIcon /> 
        },
        { 
            key: STRINGS.TASK_FILTER_KEY.ROUTINES,
            label: STRINGS.TASK_FILTER.ROUTINES, 
            icon: <RepeatRoundedIcon /> 
        },
    ];

    // Get task state and relevant task actions
    const state = useTaskState();
    const { taskCount, selectFilter } = useTasks();

    // Handler on select of task chip, changes task filter
    const handleOnSelect = (key) => {
        selectFilter(key);
    };

    // Function to return the number of tasks of the selected task, to be included in the task chip
    const getTaskCount = (key) => {
        const countKey = `${key}Count`;
        const count = Math.min(taskCount?.[countKey] ?? 0, NUMBERS.MAX_TASK_COUNT);

        if (count === NUMBERS.MAX_TASK_COUNT) return `${NUMBERS.MAX_TASK_COUNT}+`;
        else if (count === 0) return '';

        return count;
    };

    return (
        <ChipBox gap={1} aria-label='chips to change task filter'>
            {taskGroups.map(taskGroup => (
                <TaskChip
                    key={taskGroup.key}
                    label={`${taskGroup.label} ${getTaskCount(taskGroup.key)}`}
                    color='' // to force icon to use fallback color
                    icon={taskGroup.icon}
                    onClick={() => handleOnSelect(taskGroup.key)}
                    $isSelected={state.filter === taskGroup.key}
                />
            ))}
        </ChipBox>
    )
};

export default TaskChips;